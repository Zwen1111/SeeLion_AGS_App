package com.application.ags.nl.seelion.Data;

import android.app.Activity;

import com.application.ags.nl.seelion.Logic.SqlRequest;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class HistorKmDataGet implements IRoute {

    private Activity activity;
    private List<PointOfInterest> allPois;
    private SqlConnect sqlConnect;

    public HistorKmDataGet(Activity activity){
        sqlConnect = com.application.ags.nl.seelion.UI.Anchors.MapFragment.sqlConnect;

        this.activity = activity;
        generateRoute(null);
    }

    @Override
    public void generateRoute(String url) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open("historischeKilometer.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONArray array = new JSONArray(json);

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                int id = object.getInt("1");
                String title = object.getString("VVV");

                String northing = object.getString("51°35.6467’");
                String easting = object.getString("4°46.7650’ ");

                int d = Integer.parseInt(northing.substring(0, northing.indexOf("°")));
                int m = Integer.parseInt(northing.substring(northing.indexOf("°") + 1, northing.indexOf(".")));
                int s = Integer.parseInt(northing.substring(northing.indexOf(".") + 1, northing.indexOf("’")));

                Double lat = Math.signum(d) * (Math.abs(d) + (m / 60.0) + (s / 3600.0));

                d = Integer.parseInt(easting.substring(0, easting.indexOf("°")));
                m = Integer.parseInt(easting.substring(easting.indexOf("°") + 1, easting.indexOf(".")));
                s = Integer.parseInt(easting.substring(easting.indexOf(".") + 1, easting.indexOf("’")));

                Double lng = Math.signum(d) * (Math.abs(d) + (m / 60.0) + (s / 3600.0));

                LatLng latLng = new LatLng(lat, lng);

                PointOfInterest poi = new PointOfInterest(title, "", latLng);
                sqlConnect.addHistorKM(poi);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SqlRequest request = new SqlRequest();
        PointOfInterest poi =  request.getHistorKmPOI("Het Poortje");
        System.out.println(poi);
    }

    @Override
    public void getDuration() {

    }

    @Override
    public void getDistance() {

    }
}