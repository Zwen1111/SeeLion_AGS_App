package com.application.ags.nl.seelion.Logic;

import android.graphics.Color;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.application.ags.nl.seelion.Data.PointOfInterest;
import com.application.ags.nl.seelion.UI.Anchors.LanguageSelectActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class RouteCalculation {

    private PolylineOptions polylineOptions = new PolylineOptions().width(3).color(Color.RED);
    private int counter = 0;
    private List<String> urls;

    public RouteCalculation(List<PointOfInterest> pois){
        this.urls = getUrls(pois);

        for (String url : urls) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                System.out.println(response);

                try {
                    JSONArray jRoutes = response.getJSONArray("routes");
                    JSONArray jLegs = jRoutes.getJSONObject(0).getJSONArray("legs");
                    JSONArray jSteps = jLegs.getJSONObject(0).getJSONArray("steps");

                    JSONObject northEastObject = jRoutes.getJSONObject(0).getJSONObject("bounds").getJSONObject("northeast");
                    JSONObject southWestObject = jRoutes.getJSONObject(0).getJSONObject("bounds").getJSONObject("southwest");

                    LatLng northEast = new LatLng(northEastObject.getDouble("lat"), northEastObject.getDouble("lng"));
                    LatLng southWest = new LatLng(southWestObject.getDouble("lat"), southWestObject.getDouble("lng"));
                    LatLngBounds bounds = new LatLngBounds(southWest, northEast);
                    //mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 80));

                    List<List<LatLng>> lines = new ArrayList<>();

                    for (int i = 0; i < jRoutes.length(); i++) {
                        for (int j = 0; j < jLegs.length(); j++) {
                            for (int k = 0; k < jSteps.length(); k++) {
                                JSONObject object = jSteps.getJSONObject(k);
                                String polyline = object.getJSONObject("polyline").getString("points");
                                List<LatLng> list = decodePoly(polyline);
                                lines.add(list);
                            }
                        }
                    }

                    for (List<LatLng> leg : lines) {
                        polylineOptions.addAll(leg);
                    }

                    counter++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, error -> {
                System.out.println(error);
            });

            RequestQueue requestQueue = LanguageSelectActivity.requestQueue;
            requestQueue.add(jsonObjectRequest);
        }
    }

    public PolylineOptions getPolylineOptions(){
        if (counter == urls.size()) {
            return polylineOptions;
        }else{
            return new PolylineOptions();
        }
    }

    public List<String> getUrls(List<PointOfInterest> pois)
    {
        List<String> urls = new ArrayList<>();
        List<List<PointOfInterest>> listOfPois = new ArrayList<>();

        while (pois.size() > 23){
            List<PointOfInterest> subList = pois.subList(0,23);
            listOfPois.add(subList);
            pois = pois.subList(23, pois.size());
        }
        if (pois.size() > 0){
            listOfPois.add(pois);
        }
        for (List<PointOfInterest> pois2 : listOfPois) {

            LatLng origin = pois2.get(0).getLocation();
            LatLng dest = pois2.get(pois2.size()-1).getLocation();

            String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

            String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

            String trafficMode = "mode=walking";

            String str_waypoints = "waypoints=";

            for (int i = 1; i < pois2.size()-1; i++) {
                PointOfInterest poi = pois2.get(i);
                LatLng stopover = poi.getLocation();
                if (i != pois2.size()-2) {
                    str_waypoints += stopover.latitude + "," + stopover.longitude + "|";
                }else{
                    str_waypoints += stopover.latitude + "," + stopover.longitude;
                }
            }

            String parameters = str_origin + "&" + str_dest + "&" + str_waypoints + "&" + trafficMode;

            String output = "json";

            String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyCatWPTi84F26w6BYiUlUJ6sR9Tkv9VyXw";
            urls.add(url);
        }

        return urls;
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
