package com.application.ags.nl.seelion.UI.Anchors;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.application.ags.nl.seelion.Data.HistorKmDataGet;
import com.application.ags.nl.seelion.Data.PointOfInterest;
import com.application.ags.nl.seelion.Data.SqlConnect;
import com.application.ags.nl.seelion.Hardware.Gps;
import com.application.ags.nl.seelion.Logic.Map;
import com.application.ags.nl.seelion.Logic.RouteCalculation;
import com.application.ags.nl.seelion.R;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap mMap;

    private PolylineOptions polylineOptions = new PolylineOptions().width(3).color(Color.RED);

    private RouteCalculation routeCalculation;

    private Map map;
    private Gps gps;

    public MapFragment(Map map){
        this.map = map;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gps = new Gps(map, getActivity());
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
        mMap.getUiSettings().setZoomControlsEnabled(true);

        for (PointOfInterest poi : map.getPois()) {
            mMap.addMarker(new MarkerOptions().position(poi.getLocation()).title(poi.getTitle()));
        }

        routeCalculation = new RouteCalculation(map, onSuccess);
    }

    public Response.Listener<JSONObject> onSuccess = (JSONObject response) -> {
        System.out.println(response);

        try {
            JSONArray jRoutes = response.getJSONArray("routes");

            JSONObject northEastObject = jRoutes.getJSONObject(0).getJSONObject("bounds").getJSONObject("northeast");
            JSONObject southWestObject = jRoutes.getJSONObject(0).getJSONObject("bounds").getJSONObject("southwest");

            LatLng northEast = new LatLng(northEastObject.getDouble("lat"), northEastObject.getDouble("lng"));
            LatLng southWest = new LatLng(southWestObject.getDouble("lat"), southWestObject.getDouble("lng"));
            LatLngBounds bounds = new LatLngBounds(southWest, northEast);
            //mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 80));

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
}