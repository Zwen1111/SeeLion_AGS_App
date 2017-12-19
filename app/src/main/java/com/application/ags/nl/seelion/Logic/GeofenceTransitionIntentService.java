package com.application.ags.nl.seelion.Logic;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.application.ags.nl.seelion.Data.Constants;
import com.application.ags.nl.seelion.Data.PointOfInterest;
import com.application.ags.nl.seelion.Hardware.Notification;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

/**
 * Created by Lars Moesman on 14-12-2017.
 */

public class GeofenceTransitionIntentService extends IntentService {

    //todo verander string name naar interface
    private static String name;

    public GeofenceTransitionIntentService() {
        super("GEOFENCE");
    }

    //todo verander string name naar interface
    public GeofenceTransitionIntentService(String name) {
        super("GEOFENCE");
        this.name = name;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            //Todo Error handling
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            int id = Integer.parseInt(triggeringGeofences.get(0).getRequestId());
            SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
            String route = settings.getString(Constants.CURRENT_ROUTE, null);

            PointOfInterest poi;

            if (route.equals(Constants.BlindWalls)){
                poi = new SqlRequest().getBlindWallPOI(getApplicationContext(), id);
            }else{
                poi = new SqlRequest().getHistorKmPOI(getApplicationContext(), id);
            }



        //    Notification notification = new Notification();
//            notification.notifyWithBoth();
        }
//        else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
//            SharedPreferences settings = getSharedPreferences("SeeLion", 0);
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putString("Current POI", null);
//            editor.commit();
//        }
    }

}
