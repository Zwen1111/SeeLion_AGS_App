package com.application.ags.nl.seelion.Data;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.application.ags.nl.seelion.UI.Anchors.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class BlindWallsDataGet implements IRoute {

    private List<PointOfInterest> allPois;
    private SqlConnect sqlConnect;

    public BlindWallsDataGet(){
        sqlConnect = MapFragment.sqlConnect;

        allPois = new ArrayList<>();

        String url = "http://blindwalls.gallery/?json=get_posts";
        generateRoute(url);
    }

    @Override
    public void generateRoute(String url) {
        RequestQueue requestQueue = MapFragment.requestQueue;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, onSuccess, onError);

        requestQueue.add(jsonObjectRequest);
        //requestQueue.start();
    }

    @Override
    public void getDuration() {

    }

    @Override
    public void getDistance() {

    }

    public Response.Listener<JSONObject> onSuccess  = (JSONObject jsonObject) -> {
        try {
            JSONArray array = jsonObject.getJSONArray("posts");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);

                String title = object.getString("title");

                String description = object.getJSONObject("custom_fields").getJSONArray("blokken_3_tekstblok").getString(0);

                String latlng = object.getJSONObject("custom_fields").getJSONArray("blokken_2_kaart_rechts").getString(0);
                latlng = latlng.substring(latlng.indexOf("lat"));
                int index = latlng.indexOf(";s:3:");
                String latString = latlng.substring(10, index-1);
                if (String.valueOf(latString.charAt(1)) != "5"){
                    latString = latString.substring(1);
                }
                Double lat = Double.parseDouble(latString);
                latlng = latlng.substring(latlng.indexOf("lng"));
                String lngString = latlng.substring(10, latlng.lastIndexOf("\""));
                if (String.valueOf(lngString.charAt(1)) != "4"){
                    lngString = lngString.substring(1);
                }
                Double lng = Double.parseDouble(lngString);
                LatLng latLng = new LatLng(lat, lng);

                PointOfInterest pointOfInterest = new PointOfInterest(title, description, latLng);
                allPois.add(pointOfInterest);
                sqlConnect.addBlindWall(pointOfInterest);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    public Response.ErrorListener onError = (VolleyError error) -> {
        System.out.println(error);
    };
}