package com.application.ags.nl.seelion.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class SqlConnect extends SQLiteOpenHelper{

    private final Context context;
    private final String TAG = "DatabaseHandler";

    public SqlConnect(Context context){
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String Create_BlindWallsPOI_Table = "CREATE TABLE " + Constants.BLIND_WALLS_TABLE_NAME
                + " (" + Constants.KEY_ID + " REAL PRIMARY KEY, "
                + Constants.KEY_TITLE + " TEXT, "
                + Constants.KEY_DESCRIPTION + " TEXT, "
                + Constants.KEY_LAT + " REAL, "
                + Constants.KEY_LNG + " REAL, "
                + Constants.KEY_IMAGES + " TEXT)";

        sqLiteDatabase.execSQL(Create_BlindWallsPOI_Table);

        Log.i(TAG, "onCreate: " + Create_BlindWallsPOI_Table);

        String Create_HistorKM_Table = "CREATE TABLE " + Constants.HISTOR_KM_TABLE_NAME
                + " (" + Constants.KEY_ID + " REAL PRIMARY KEY, "
                + Constants.KEY_TITLE + " TEXT, "
                + Constants.KEY_DESCRIPTION + " TEXT, "
                + Constants.KEY_LAT + " REAL, "
                + Constants.KEY_LNG + " REAL, "
                + Constants.KEY_IMAGES + " TEXT)";

        sqLiteDatabase.execSQL(Create_HistorKM_Table);

        Log.i(TAG, "onCreate: " + Create_HistorKM_Table);

        String Create_WalkedRoute_Table = "CREATE TABLE " + Constants.WALKED_ROUTE_TABLE_NAME
                + " (" + Constants.KEY_LAT + " REAL, "
                + Constants.KEY_LNG + " REAL)";

        sqLiteDatabase.execSQL(Create_WalkedRoute_Table);

        Log.i(TAG, "onCreate: " + Create_WalkedRoute_Table);

        String Create_Visited_Poi_Table = "CREATE TABLE " + Constants.VISITED_POI_TABLE_NAME
                + " (" + Constants.KEY_ID + " REAL PRIMARY KEY, "
                + Constants.KEY_TITLE + " TEXT, "
                + Constants.KEY_DESCRIPTION + " TEXT, "
                + Constants.KEY_LAT + " REAL, "
                + Constants.KEY_LNG + " REAL, "
                + Constants.KEY_IMAGES + " TEXT)";

        sqLiteDatabase.execSQL(Create_Visited_Poi_Table);

        Log.i(TAG, "onCreate: " + Create_Visited_Poi_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.BLIND_WALLS_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.HISTOR_KM_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addBlindWall(PointOfInterest poi)
    {
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ID, poi.getId());
        values.put(Constants.KEY_TITLE, poi.getTitle());
        values.put(Constants.KEY_DESCRIPTION, poi.getDescription());
        values.put(Constants.KEY_LAT, poi.getLocation().latitude);
        values.put(Constants.KEY_LNG, poi.getLocation().longitude);
        values.put(Constants.KEY_IMAGES, poi.getImagesJsonString());

        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(Constants.BLIND_WALLS_TABLE_NAME, null, values);

        Log.d(TAG,"addBlindWall: inserted " + poi.toString());
    }

    public void addHistorKM(PointOfInterest poi)
    {
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ID, poi.getId());
        values.put(Constants.KEY_TITLE, poi.getTitle());
        values.put(Constants.KEY_DESCRIPTION, poi.getDescription());
        values.put(Constants.KEY_LAT, poi.getLocation().latitude);
        values.put(Constants.KEY_LNG, poi.getLocation().longitude);
        values.put(Constants.KEY_IMAGES, poi.getImagesJsonString());

        SQLiteDatabase database = getWritableDatabase();
        database.insert(Constants.HISTOR_KM_TABLE_NAME, null, values);

        Log.d(TAG,"addHistorKM: inserted " + poi.toString());
    }

    public void addWalkedRouteLocation(LatLng location){
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_LAT, location.latitude);
        values.put(Constants.KEY_LNG, location.longitude);

        SQLiteDatabase database = getWritableDatabase();
        database.insert(Constants.WALKED_ROUTE_TABLE_NAME, null, values);

        Log.d(TAG,"addWalkedRouteLocation: inserted " + location.toString());
    }

    public void addWalkedRouteLocations(List<LatLng> locations){
        for (LatLng location : locations) {
            ContentValues values = new ContentValues();
            values.put(Constants.KEY_LAT, location.latitude);
            values.put(Constants.KEY_LNG, location.longitude);

            SQLiteDatabase database = getWritableDatabase();
            database.insert(Constants.WALKED_ROUTE_TABLE_NAME, null, values);

            Log.d(TAG,"addWalkedRouteLocation: inserted " + location.toString());
        }
    }

    public void addVisitedPoi(PointOfInterest poi){
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ID, poi.getId());
        values.put(Constants.KEY_TITLE, poi.getTitle());
        values.put(Constants.KEY_DESCRIPTION, poi.getDescription());
        values.put(Constants.KEY_LAT, poi.getLocation().latitude);
        values.put(Constants.KEY_LNG, poi.getLocation().longitude);
        values.put(Constants.KEY_IMAGES, poi.getImagesJsonString());

        SQLiteDatabase database = getWritableDatabase();
        database.insert(Constants.VISITED_POI_TABLE_NAME, null, values);

        Log.d(TAG,"addVisitedPoi: inserted " + poi.toString());
    }
}
