package com.application.ags.nl.seelion.Hardware;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.application.ags.nl.seelion.Data.Constants;
import com.application.ags.nl.seelion.Data.PointOfInterest;
import com.application.ags.nl.seelion.Logic.GeofenceTransitionIntentService;
import com.application.ags.nl.seelion.Logic.Map;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class Gps implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    private Map map;
    private Context context;
    private GoogleApiClient googleApiClient;
    private List<Geofence> geofenceList;
    private GeofencingClient geofencingClient;

    public Gps(Map map, Context context) {
        this.map = map;
        this.context = context;

        geofenceList = new ArrayList<>();

        geofencingClient = LocationServices.getGeofencingClient(context);
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        if (!googleApiClient.isConnecting() || !googleApiClient.isConnected()) {
            googleApiClient.connect();
        }


    }

    private void makeGeofences(List<PointOfInterest> pointOfInterests) {
        for (PointOfInterest poi : pointOfInterests) {
            Log.i("POI", poi.getLocation().toString());
            geofenceList.add(new Geofence.Builder()
                    .setRequestId(String.valueOf(poi.getId()))
                    .setCircularRegion(
                            poi.getLocation().latitude,
                            poi.getLocation().longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }
        Log.i("List", "" + geofenceList.size());
        addGeofences();
    }

    private void addGeofences() {
        if (!googleApiClient.isConnected()) {
            return;
        }
        try {
            //geofencingClient.addGeofences(getGeofenicingRequest(), getGeofencePendingIntent());
           LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                   getGeofenicingRequest(),
                   getGeofencePendingIntent()).setResultCallback(this);
        } catch (SecurityException securityException) {
            //Todo Error handling
        }
    }

    private GeofencingRequest getGeofenicingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();

    }

    private PendingIntent getGeofencePendingIntent() {
        GeofenceTransitionIntentService geofenceTransitionIntentService = new GeofenceTransitionIntentService("Toet"); //Todo verander toet door interface
        Intent intent = new Intent(context, geofenceTransitionIntentService.getClass());

        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private List<Geofence> getGeofenceList() {
        return geofenceList;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        makeGeofences(map.getPois());
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("GPS", "Not Connected");
    }

    @Override
    public void onResult(@NonNull Status status) {

        if (status.isSuccess()) {
            Log.i("GPS Satus", "" + status.getStatusCode());
        } else {

        }
    }
}
