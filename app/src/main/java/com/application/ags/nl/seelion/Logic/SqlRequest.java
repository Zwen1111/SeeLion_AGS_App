package com.application.ags.nl.seelion.Logic;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

import com.application.ags.nl.seelion.Data.Constants;
import com.application.ags.nl.seelion.Data.PointOfInterest;
import com.application.ags.nl.seelion.Data.SqlConnect;
import com.application.ags.nl.seelion.UI.Anchors.LanguageSelectActivity;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class SqlRequest {

    private SqlConnect sqlConnect;

    public SqlRequest(){
        sqlConnect = LanguageSelectActivity.sqlConnect;
    }

    public PointOfInterest getBlindWallPOI(Context context, String title){
        SQLiteDatabase db = sqlConnect.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.BLIND_WALLS_TABLE_NAME + " WHERE " + Constants.KEY_TITLE + "='" + title + "'", null);

        List<String> drawables = new ArrayList<>();
        cursor.moveToFirst();
        try {
            JSONArray array = new JSONArray(cursor.getString(cursor.getColumnIndex(Constants.KEY_IMAGES)));

            for (int i = 0; i < array.length(); i++) {
                String image = array.getString(i);
                drawables.add(image);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        cursor.moveToFirst();
        PointOfInterest poi = new PointOfInterest(
                cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE)),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)),
                new LatLng(cursor.getDouble(cursor.getColumnIndex(Constants.KEY_LAT)),
                        cursor.getDouble(cursor.getColumnIndex(Constants.KEY_LNG))),
                drawables
        );

        return poi;
    }

    public PointOfInterest getHistorKmPOI(Context context, String title){
        SQLiteDatabase db = sqlConnect.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.HISTOR_KM_TABLE_NAME + " WHERE " + Constants.KEY_TITLE + "='" + title + "'", null);

        List<String> drawables = new ArrayList<>();
        cursor.moveToFirst();
        try {
            JSONArray array = new JSONArray(cursor.getString(cursor.getColumnIndex(Constants.KEY_IMAGES)));

            for (int i = 0; i < array.length(); i++) {
                String image = array.getString(i);
                drawables.add(image);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PointOfInterest poi = new PointOfInterest(
                cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE)),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)),
                new LatLng(cursor.getDouble(cursor.getColumnIndex(Constants.KEY_LAT)),
                        cursor.getDouble(cursor.getColumnIndex(Constants.KEY_LNG))),
                drawables
        );

        return poi;
    }

    public List<PointOfInterest> getHistorKmPois(Context context){
        SQLiteDatabase db = sqlConnect.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.HISTOR_KM_TABLE_NAME, null);

        List<PointOfInterest> pois = new ArrayList<>();

        if (cursor.moveToFirst()){
            while (cursor.moveToNext()){
                List<String> drawables = new ArrayList<>();
                try {
                    String json = cursor.getString(cursor.getColumnIndex(Constants.KEY_IMAGES));
                    JSONArray array = new JSONArray(json);

                    for (int i = 0; i < array.length(); i++) {
                        String image = array.getString(i);
                        drawables.add(image);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                PointOfInterest poi = new PointOfInterest(
                        cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)),
                        new LatLng(cursor.getDouble(cursor.getColumnIndex(Constants.KEY_LAT)), cursor.getDouble(cursor.getColumnIndex(Constants.KEY_LNG))),
                        drawables
                );
                pois.add(poi);
            }
        }

        return pois;
    }

    public List<PointOfInterest> getBlindWallsPois(Context context){
        SQLiteDatabase db = sqlConnect.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.BLIND_WALLS_TABLE_NAME, null);

        List<PointOfInterest> pois = new ArrayList<>();

        if (cursor.moveToFirst()){
            List<String> drawables = new ArrayList<>();
            while (cursor.moveToNext()){
                try {
                    JSONArray array = new JSONArray(cursor.getString(cursor.getColumnIndex(Constants.KEY_IMAGES)));

                    for (int i = 0; i < array.length(); i++) {
                        String image = array.getString(i);
                        drawables.add(image);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                PointOfInterest poi = new PointOfInterest(
                        cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)),
                        new LatLng(cursor.getDouble(cursor.getColumnIndex(Constants.KEY_LAT)), cursor.getDouble(cursor.getColumnIndex(Constants.KEY_LNG))),
                        drawables
                );
                pois.add(poi);
            }
        }

        return pois;
    }
}