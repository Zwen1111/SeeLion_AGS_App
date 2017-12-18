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

    private List<String> urls;
    private int counter;

    public RouteCalculation(Map map, Response.Listener onSuccess){
        this.urls = getUrls(map.getPois());

        counter = 0;

        for (String url : urls) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, onSuccess, error -> System.out.println(error));

            RequestQueue requestQueue = LanguageSelectActivity.requestQueue;
            requestQueue.add(jsonObjectRequest);
        }
    }

    public List<String> getUrls(List<PointOfInterest> pois)
    {
        List<String> urls = new ArrayList<>();

        List<List<PointOfInterest>> subLists = new ArrayList<>();

        while (pois.size() > 23){
            List<PointOfInterest> subList = pois.subList(0,22);
            subLists.add(subList);
            pois = pois.subList(22, pois.size()-1);
        }
        if (pois.size() > 0){
            subLists.add(pois);
        }

        for (int i = 0; i < subLists.size(); i++) {
            List<PointOfInterest> subList = subLists.get(i);

            LatLng origin = subList.get(0).getLocation();
            LatLng dest = subList.get(subList.size()-1).getLocation();

            String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

            String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

            String trafficMode = "mode=walking";

            String str_waypoints = "waypoints=optimize:true|";

            for (int j = 1; j < subList.size()-1; j++) {
                PointOfInterest poi = subList.get(j);
                LatLng stopover = poi.getLocation();
                if (j != subList.size()-2) {
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

//        for (int i = 1; i < pois.size(); i++) {
//            LatLng origin = pois.get(i-1).getLocation();
//            LatLng dest = pois.get(i).getLocation();
//
//            String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//
//            String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//
//            String trafficMode = "mode=walking";
//
//            String parameters = str_origin + "&" + str_dest + "&" + trafficMode;
//
//            String output = "json";
//
//            String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyCatWPTi84F26w6BYiUlUJ6sR9Tkv9VyXw";
//            urls.add(url);
//        }
        return urls;
    }

    public List<LatLng> decodePoly(String encoded) {

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
