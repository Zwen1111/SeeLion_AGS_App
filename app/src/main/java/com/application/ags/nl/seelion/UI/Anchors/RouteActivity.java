package com.application.ags.nl.seelion.UI.Anchors;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.application.ags.nl.seelion.Data.Constants;
import com.application.ags.nl.seelion.Data.PointOfInterest;
import com.application.ags.nl.seelion.Hardware.Notification;
import com.application.ags.nl.seelion.Logic.Map;
import com.application.ags.nl.seelion.Logic.Reset;
import com.application.ags.nl.seelion.Logic.SqlRequest;
import com.application.ags.nl.seelion.R;
import com.application.ags.nl.seelion.UI.Links.RouteAdapter;
import com.application.ags.nl.seelion.UI.popups.Error;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.Locale;

import static com.application.ags.nl.seelion.UI.Anchors.LanguageSelectActivity.sqlConnect;


public class RouteActivity extends AppCompatActivity {

    public ImageButton routePointButton;
    public ImageButton mapButton;
    public ImageButton detailButton;
    public FrameLayout frameLayout;
    private RouteAdapter routeAdapter;
    private PointOfInterest currentPOI;
    public MapFragment mapFragment = null;
    private boolean done;
    private SharedPreferences settings;

    public enum Fragments {
        MAP, DETAIL, POINTS
    }

    private Map map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        settings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Current POI", null);
        editor.commit();

        String mapString = getIntent().getStringExtra("MAP");

        switch (mapString) {
            case Constants.BlindWalls:
                map = Map.generateBlindWallsMap(this);
                break;
            case Constants.HistorKm:
                map = Map.generateHistorKmMap(this);
                break;
        }

        if (map.getPois().size() > 0) {
            currentPOI = map.getPois().get(0);
        }

        routePointButton = findViewById(R.id.imageButton_routepoint);
        mapButton = findViewById(R.id.imageButton_map);
        detailButton = findViewById(R.id.imageButton_detail);
        frameLayout = findViewById(R.id.fragment_frame);
        routeAdapter = new RouteAdapter(this);

        changeFragment(Fragments.MAP);
    }

    public void triggerNotification() {
      NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          NotificationChannel notificationChannel = new NotificationChannel("SEELION_ID", "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
          notificationChannel.setDescription("Channels description");
          notificationChannel.enableLights(true);
          notificationChannel.setLightColor(Color.RED);
          notificationManager.createNotificationChannel(notificationChannel);
      }

      NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "SEELION_ID")
              .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
              .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
              .setColor(Color.RED)
              .setContentTitle("SeeLion")
              .setContentText(getString(R.string.notification_text));

      notificationManager.notify(1, builder.build());

    }

    public void changeFragment(Fragments fragment) {

        Fragment newFragment = null;
        boolean canChange = true;

        switch (fragment) {
            case MAP:
                if (mapFragment == null) {
                    mapFragment = new MapFragment(this, map);
                }
                newFragment = mapFragment;
                break;
            case DETAIL:
                newFragment = new DetailPointFragment(currentPOI);
                break;
            case POINTS:
                boolean isHistorKm = false;
                if(settings.getString(Constants.CURRENT_ROUTE, "").equals(Constants.HistorKm)){
                    isHistorKm = true;
                }
                newFragment = new RoutePointsFragment(this, map, isHistorKm);
                break;
        }

        if (canChange == false) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.detailfragment_toast, Toast.LENGTH_LONG
            ).show();
            return;
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(frameLayout.getId(), newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    public void setSelectedPOI(PointOfInterest currentPOI){
        this.currentPOI = currentPOI;
    }

    public void setCurrentPOI(PointOfInterest currentPOI){
        this.currentPOI = currentPOI;
        mapFragment.changeMarker(currentPOI);
        int counter = 0;
        for (int i = 0; i < map.getPois().size(); i++) {
            if (map.getPois().get(i) == currentPOI){
                map.getPois().get(i).setVisited(true);
            }
            if (map.getPois().get(i).isVisited()){
                counter++;
            }else{
                counter = 0;
            }
        }

        sqlConnect.addVisitedPoi(currentPOI);

        if (counter == map.getPois().size()){
            done = true;
        }

        routeAdapter.onClick(findViewById(R.id.imageButton_detail));
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = Error.generateError(this, getString(R.string.exit_route), getString(R.string.exit_route_description));
        builder.setNegativeButton(getString(R.string.yes), (dialogInterface, i) -> {
            SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("Save exit", true);
            editor.commit();

            new SqlRequest().clearWalkedLocations();
            new SqlRequest().clearVisitedPois();
            super.onBackPressed();
        });
        builder.setPositiveButton(getString(R.string.no), (dialogInterface, i) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.changeLanguage:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                ArrayAdapter<String> languages = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item);
                languages.add("English");
                languages.add("Nederlands");
                builder.setAdapter(languages, (dialogInterface, i) -> {
                    if(languages.getItem(i).equals("English")){
                        Locale myLocale = new Locale("en");
                        Resources res = getResources();
                        DisplayMetrics dm = res.getDisplayMetrics();
                        Configuration conf = res.getConfiguration();
                        conf.locale = myLocale;
                        res.updateConfiguration(conf, dm);
                    }else{
                        Locale myLocale = new Locale("nl");
                        Resources res = getResources();
                        DisplayMetrics dm = res.getDisplayMetrics();
                        Configuration conf = res.getConfiguration();
                        conf.locale = myLocale;
                        res.updateConfiguration(conf, dm);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.colorBlind:
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                boolean checked = !item.isChecked();
                if (checked) {
                    editor.putFloat("MARKER_COLOR", BitmapDescriptorFactory.HUE_BLUE);
                    editor.putInt("WALKED_ROUTE_COLOR", Color.BLUE);
                    editor.putInt("ROUTE_COLOR", Color.YELLOW);
                }else {
                    editor.putFloat("MARKER_COLOR", BitmapDescriptorFactory.HUE_GREEN);
                    editor.putInt("WALKED_ROUTE_COLOR", Color.GREEN);
                    editor.putInt("ROUTE_COLOR", Color.RED);
                }
                editor.commit();
                item.setChecked(checked);
                mapFragment.redrawMap();
                return true;
            case R.id.reset:
                new Reset(this);
                mapFragment.redrawMap();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}