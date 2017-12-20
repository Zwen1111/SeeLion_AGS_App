package com.application.ags.nl.seelion.Data;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.application.ags.nl.seelion.Logic.SqlRequest;
import com.application.ags.nl.seelion.UI.Anchors.LanguageSelectActivity;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class HistorKmDataGet implements IRoute {

    private Context context;
    private List<PointOfInterest> allPois;
    private SqlConnect sqlConnect;

    public HistorKmDataGet(Context context){
        sqlConnect = LanguageSelectActivity.sqlConnect;

        this.context = context;
        generateRoute();
    }

    private void generateRoute() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("historischeKilometer.json");
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

                double lat = object.getDouble("51°35.6467’");
                double lng = object.getDouble("4°46.7650’ ");

                LatLng latLng = new LatLng(lat, lng);

                List<String> drawables = new ArrayList<>();

                JSONArray images = object.getJSONArray("Image");
                for (int j = 0; j < images.length(); j++) {
                    JSONObject imagesObject = images.getJSONObject(j);
                    String image = imagesObject.getString("image");
                    drawables.add(image);
                }

                PointOfInterest poi = new PointOfInterest(id, title, "", latLng, drawables);
                if (poi.getTitle().length() > 0) {
                    sqlConnect.addHistorKM(poi);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getDuration() {

    }

    @Override
    public void getDistance() {

    }
}