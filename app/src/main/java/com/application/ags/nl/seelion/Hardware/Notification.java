package com.application.ags.nl.seelion.Hardware;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import com.application.ags.nl.seelion.R;

/**
 * Created by Heint on 14-12-2017.
 */

public class Notification extends FragmentActivity {

    private PendingIntent pendingIntent;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrival);

        Intent intent = new Intent(this, this.getClass());
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void notifyWithSound() {
        //deprecated, may cause errors
        builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Sound Notification");

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);
        notificationManager.notify(1, builder.build());
    }

    public void notifyWithVibration() {
        //deprecated, may cause errors
        builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Vibration Notification");

        long[] vibrationArray = {200};
        builder.setVibrate(vibrationArray);
        notificationManager.notify(1, builder.build());
    }

    public void notifyWithBoth() {
        //deprecated, may cause errors
        builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Vibration Notification");

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);

        long[] vibrationArray = {200};
        builder.setVibrate(vibrationArray);
        notificationManager.notify(1, builder.build());
    }
}

