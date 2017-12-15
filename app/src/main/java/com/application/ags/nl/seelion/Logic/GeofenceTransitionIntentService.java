package com.application.ags.nl.seelion.Logic;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.application.ags.nl.seelion.Hardware.Notification;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

/**
 * Created by Lars Moesman on 14-12-2017.
 */

public class GeofenceTransitionIntentService extends IntentService {

    public GeofenceTransitionIntentService(String name) {
        super(name);
    }

    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            //Todo Error handling
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            Notification notification = new Notification();
            notification.notifyWithBoth();
        } else {
            // Todo error.
        }
    }

}
