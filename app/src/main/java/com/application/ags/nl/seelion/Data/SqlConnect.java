package com.application.ags.nl.seelion.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

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
                + " (" + Constants.KEY_TITLE + " TEXT, "
                + Constants.KEY_DESCRIPTION + " TEXT, "
                + Constants.KEY_LAT + " REAL, "
                + Constants.KEY_LNG + " REAL)";

        sqLiteDatabase.execSQL(Create_BlindWallsPOI_Table);

        Log.i(TAG, "onCreate: " + Create_BlindWallsPOI_Table);

        String Create_HistorKM_Table = "CREATE TABLE " + Constants.HISTOR_KM_TABLE_NAME
                + " (" + Constants.KEY_TITLE + " TEXT, "
                + Constants.KEY_DESCRIPTION + " TEXT, "
                + Constants.KEY_LAT + " REAL, "
                + Constants.KEY_LNG + " REAL)";

        sqLiteDatabase.execSQL(Create_HistorKM_Table);

        Log.i(TAG, "onCreate: " + Create_HistorKM_Table);
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
        values.put(Constants.KEY_TITLE, poi.getTitle());
        values.put(Constants.KEY_DESCRIPTION, poi.getDescription());
        values.put(Constants.KEY_LAT, poi.getLocation().latitude);
        values.put(Constants.KEY_LNG, poi.getLocation().longitude);

        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(Constants.BLIND_WALLS_TABLE_NAME, null, values);

        Log.d(TAG,"addBlindWall: inserted " + poi.toString());
    }

    public void addHistorKM(PointOfInterest poi)
    {
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_TITLE, poi.getTitle());
        values.put(Constants.KEY_DESCRIPTION, poi.getDescription());
        values.put(Constants.KEY_LAT, poi.getLocation().latitude);
        values.put(Constants.KEY_LNG, poi.getLocation().longitude);

        SQLiteDatabase database = getWritableDatabase();
        database.insert(Constants.HISTOR_KM_TABLE_NAME, null, values);

        Log.d(TAG,"addHistorKM: inserted " + poi.toString());
    }
}
