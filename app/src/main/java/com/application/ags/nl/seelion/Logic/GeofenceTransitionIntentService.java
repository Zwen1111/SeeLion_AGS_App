package com.application.ags.nl.seelion.Logic;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.application.ags.nl.seelion.Data.Constants;
import com.application.ags.nl.seelion.Data.PointOfInterest;
import com.application.ags.nl.seelion.Hardware.Notification;
import com.application.ags.nl.seelion.R;
import com.application.ags.nl.seelion.UI.Anchors.RouteActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

/**
 * Created by Lars Moesman on 14-12-2017.
 */

public class GeofenceTransitionIntentService extends IntentService {

    private static RouteActivity routeActivity;

    public GeofenceTransitionIntentService() {
        super("GEOFENCE");

    }

    public GeofenceTransitionIntentService(RouteActivity routeActivity) {
        super("GEOFENCE");
        this.routeActivity = routeActivity;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Toast.makeText(routeActivity.getApplicationContext(), R.string.error_key, Toast.LENGTH_LONG).show();
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

            boolean alreadyVisited = false;

            List<PointOfInterest> visitedPois = new SqlRequest().getVisitedPois();
            Log.i("Visited POI's", visitedPois.toString());
            for (PointOfInterest visitedPoi : visitedPois) {
                if (visitedPoi.getTitle().equals(poi.getTitle())){
                    alreadyVisited = true;
                }
            }

            if (!alreadyVisited) {
                routeActivity.setCurrentPOI(poi);
            }

            //Todo: Notification
        }
//        else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
//            SharedPreferences settings = getSharedPreferences("SeeLion", 0);
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putString("Current POI", null);
//            editor.commit();
//        }
    }

}
