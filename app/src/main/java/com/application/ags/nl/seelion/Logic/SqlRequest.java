package com.application.ags.nl.seelion.Logic;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.application.ags.nl.seelion.Data.Constants;
import com.application.ags.nl.seelion.Data.PointOfInterest;
import com.application.ags.nl.seelion.Data.SqlConnect;
import com.application.ags.nl.seelion.UI.Anchors.LanguageSelectActivity;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

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

    public PointOfInterest getBlindWallPOI(String title){
        SQLiteDatabase db = sqlConnect.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.BLIND_WALLS_TABLE_NAME + " WHERE " + Constants.KEY_TITLE + "='" + title + "'", null);

        cursor.moveToFirst();
        PointOfInterest poi = new PointOfInterest(
                cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE)),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)),
                new LatLng(cursor.getDouble(cursor.getColumnIndex(Constants.KEY_LAT)),
                        cursor.getDouble(cursor.getColumnIndex(Constants.KEY_LNG)))
        );

        return poi;
    }

    public PointOfInterest getHistorKmPOI(String title){
        SQLiteDatabase db = sqlConnect.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.HISTOR_KM_TABLE_NAME + " WHERE " + Constants.KEY_TITLE + "='" + title + "'", null);

        cursor.moveToFirst();
        PointOfInterest poi = new PointOfInterest(
                cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE)),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)),
                new LatLng(cursor.getDouble(cursor.getColumnIndex(Constants.KEY_LAT)),
                        cursor.getDouble(cursor.getColumnIndex(Constants.KEY_LNG)))
        );

        return poi;
    }

    public List<PointOfInterest> getHistorKmPois(){
        SQLiteDatabase db = sqlConnect.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.HISTOR_KM_TABLE_NAME, null);

        List<PointOfInterest> pois = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                PointOfInterest poi = new PointOfInterest(
                        cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)),
                        new LatLng(cursor.getDouble(cursor.getColumnIndex(Constants.KEY_LAT)),
                                cursor.getDouble(cursor.getColumnIndex(Constants.KEY_LNG)))
                );
                pois.add(poi);
            }
        }

        return pois;
    }

    public List<PointOfInterest> getBlindWallsPois(){
        SQLiteDatabase db = sqlConnect.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.BLIND_WALLS_TABLE_NAME, null);

        List<PointOfInterest> pois = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                PointOfInterest poi = new PointOfInterest(
                        cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)),
                        new LatLng(cursor.getDouble(cursor.getColumnIndex(Constants.KEY_LAT)),
                                cursor.getDouble(cursor.getColumnIndex(Constants.KEY_LNG)))
                );
                pois.add(poi);
            }
        }

        return pois;
    }
}