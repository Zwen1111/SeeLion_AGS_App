package com.application.ags.nl.seelion.UI.Anchors;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.application.ags.nl.seelion.Data.Constants;
import com.application.ags.nl.seelion.Data.PointOfInterest;
import com.application.ags.nl.seelion.Data.SqlConnect;
import com.application.ags.nl.seelion.Hardware.Gps;
import com.application.ags.nl.seelion.Logic.Map;
import com.application.ags.nl.seelion.Logic.RouteCalculation;
import com.application.ags.nl.seelion.Logic.SqlRequest;
import com.application.ags.nl.seelion.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.LOCATION_SERVICE;

@SuppressLint("ValidFragment")
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, SensorEventListener {

    private MapView mapView;
    private GoogleMap mMap;

    private PolylineOptions polylineOptions;

    private RouteCalculation routeCalculation;

    private Map map;
    private Gps gps;
    private RouteActivity routeActivity;

    private SensorManager sensorService;
    private Sensor sensor;
    private LocationManager locationManager;

    private int degree;

    private List<LatLng> walked;

    private List<Marker> markers;

    public MapFragment(RouteActivity routeActivity, Map map) {
        this.map = map;
        this.routeActivity = routeActivity;
        walked = new ArrayList<>();
        markers = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager)getActivity().getApplicationContext().getSystemService(LOCATION_SERVICE);
        gps = new Gps(routeActivity, map, getActivity().getApplicationContext());

        sensorService = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorService.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("yo", "yo");
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        Log.i("yo", "" + view.getId());
        if (mapView != null) {

        }
        mapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);
        return view;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
  /*  @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("String", ":");
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, 42);
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        if (sensor != null){
            sensorService.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }

        SharedPreferences settings = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        for (PointOfInterest poi : map.getPois()) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(poi.getLocation()).title(poi.getTitle()).icon(BitmapDescriptorFactory.defaultMarker(settings.getFloat("MARKER_COLOR", -1))));
            markers.add(marker);
        }

        Location location = getLastKnownLocation();

        polylineOptions = new PolylineOptions().width(3).color(Color.RED);
        routeCalculation = new RouteCalculation(map, new LatLng(location.getLatitude(), location.getLongitude()), onSuccess);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(18)
                .bearing(degree)
                .tilt(45)
                .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        SharedPreferences.Editor editor = settings.edit();

        if (!settings.getBoolean("Save exit", true)){
            List<LatLng> walkedLocations = new SqlRequest().getWalkedLocations();
            PolylineOptions options = new PolylineOptions()
                    .width(3)
                    .color(Color.BLUE);
            for (int i = 1; i < walkedLocations.size(); i++) {
                List<LatLng> leg = new ArrayList<>(2);
                leg.add(walkedLocations.get(i-1));
                leg.add(walkedLocations.get(i));
                options.addAll(leg);
            }
            getActivity().runOnUiThread(() -> mMap.addPolyline(options));
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (walked.size() > 1){
                    List<LatLng> walkedLocations = walked;

                    int color = settings.getInt("WALKED_ROUTE_COLOR", -1);

                    PolylineOptions options = new PolylineOptions()
                            .width(3)
                            .color(color);
                    for (int i = 1; i < walkedLocations.size(); i++) {
                        List<LatLng> leg = new ArrayList<>(2);
                        leg.add(walkedLocations.get(i-1));
                        leg.add(walkedLocations.get(i));
                        options.addAll(leg);
                    }
                    getActivity().runOnUiThread(() -> mMap.addPolyline(options));

                    new SqlConnect(getActivity()).addWalkedRouteLocations(walkedLocations);

                    LatLng lastLocation = walkedLocations.get(walkedLocations.size()-1);
                    walked = new ArrayList<>();
                    walked.add(lastLocation);
                }
                if (routeActivity.isDone()){
                    editor.putBoolean("Save exit", true);
                    editor.commit();
                }
            }
        }, 3000, 3000);

        editor.putBoolean("Save exit", false);
        editor.commit();
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        Location location = getLastKnownLocation();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(18)
                .bearing(degree)
                .tilt(45)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        return true;
    }

    public Response.Listener<JSONObject> onSuccess = (JSONObject response) -> {
        System.out.println(response);

        try {
            JSONArray jRoutes = response.getJSONArray("routes");

            List<List<LatLng>> lines = new ArrayList<>();

            for (int i = 0; i < jRoutes.length(); i++) {
                JSONArray jLegs = jRoutes.getJSONObject(i).getJSONArray("legs");
                for (int j = 0; j < jLegs.length(); j++) {
                    JSONArray jSteps = jLegs.getJSONObject(j).getJSONArray("steps");
                    for (int k = 0; k < jSteps.length(); k++) {
                        JSONObject object = jSteps.getJSONObject(k);
                        String polyline = object.getJSONObject("polyline").getString("points");
                        List<LatLng> list = routeCalculation.decodePoly(polyline);
                        lines.add(list);
                    }
                }
            }

            for (List<LatLng> leg : lines) {
                polylineOptions.addAll(leg);
            }

            mMap.addPolyline(polylineOptions);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        if (mMap != null && sensor != null){
            sensorService.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        sensorService.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int degree = Math.round(sensorEvent.values[0]);

        if (degree != this.degree && (degree - this.degree > 10 || degree - this.degree < -10)) {
            Location location = getLastKnownLocation();

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(18)
                    .bearing(degree)
                    .tilt(45)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            this.degree = degree;

            if (walked.size() > 0) {
                LatLng lastLocation = walked.get(walked.size() - 1);

                float[] results = new float[1];

                Location.distanceBetween(lastLocation.latitude, lastLocation.longitude, location.getLatitude(), location.getLongitude(), results);

                if (results[0] > 2){
                    walked.add(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            }else{
                walked.add(new LatLng(location.getLatitude(), location.getLongitude()));
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, Constants.PERMISSION_REQUEST_CODE);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.PERMISSION_REQUEST_CODE);
        }
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    public void changeMarker(PointOfInterest currentPOI) {
        for (int i = 0; i < map.getPois().size(); i++) {
            if (map.getPois().get(i).getTitle().equals(currentPOI.getTitle())){
                map.getPois().get(i).setVisited(true);
            }
        }
        for (Marker marker : markers) {
            if (marker.getTitle().equals(currentPOI.getTitle())){
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                float color = sharedPreferences.getFloat("MARKER_COLOR", -1);
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(color));
            }
        }
    }

    public void setColorblind(){
        onMapReady(mMap);
    }
}