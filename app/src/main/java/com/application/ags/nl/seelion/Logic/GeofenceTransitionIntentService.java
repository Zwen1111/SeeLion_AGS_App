package com.application.ags.nl.seelion.Logic;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.application.ags.nl.seelion.Hardware.Notification;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

/**
 * Created by Lars Moesman on 14-12-2017.
 */

public class GeofenceTransitionIntentService extends IntentService {

    public GeofenceTransitionIntentService() {
        super("GEOFENCE");
        Log.i("Geofence", "Geofences");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("Geofence", "Geofence");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            //Todo Error handling
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            SharedPreferences settings = getSharedPreferences("SeeLion", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("Current POI", triggeringGeofences.get(0).getRequestId());
            editor.commit();

        //    Notification notification = new Notification();
//            notification.notifyWithBoth();
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
            SharedPreferences settings = getSharedPreferences("SeeLion", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("Current POI");
            editor.putString("Current POI", null);
            editor.commit();
        }
    }

}
