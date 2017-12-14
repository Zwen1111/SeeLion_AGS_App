package com.application.ags.nl.seelion.Data;

/**
 * Created by zwen1 on 12/11/2017.
 */

public class Constants
{
    public static enum length { KILOMETER, MILE};

    public static final int PERMISSION_REQUEST_CODE = 698;

    public static final int DB_VERSION = 8;
    public static final String DB_NAME = "LocationAwareDB";

    //tables:
    public static final String BLIND_WALLS_TABLE_NAME = "BlindWallsPOI";
    public static final String HISTOR_KM_TABLE_NAME = "HistorKmPOI";

    //columns:
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_LAT = "latitude";
    public static final String KEY_LNG = "longitude";

    //geofence:
    public static final float GEOFENCE_RADIUS_IN_METERS = 20;
}