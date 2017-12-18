package com.application.ags.nl.seelion.UI.Anchors;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.application.ags.nl.seelion.Data.Constants;
import com.application.ags.nl.seelion.Data.PointOfInterest;
import com.application.ags.nl.seelion.Logic.GeofenceTransitionIntentService;

import com.application.ags.nl.seelion.Logic.Map;
import com.application.ags.nl.seelion.Logic.RouteCalculation;
import com.application.ags.nl.seelion.R;
import com.application.ags.nl.seelion.UI.Links.RouteAdapter;
import com.application.ags.nl.seelion.UI.popups.Error;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class RouteActivity extends AppCompatActivity {

    public ImageButton routePointButton;
    public ImageButton mapButton;
    public ImageButton detailButton;
    public FrameLayout frameLayout;
    private RouteAdapter routeAdapter;
    public enum Fragments {
        MAP, DETAIL, POINTS
    }

    private Map map;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        String mapString = getIntent().getStringExtra("MAP");

        switch (mapString){
            case Constants.BlindWalls:
                Log.i("test", "test");
                map = Map.generateBlindWallsMap(this);
                break;
            case Constants.HistorKm:
                map = Map.generateHistorKmMap(this);
                break;
        }

        routePointButton = findViewById(R.id.imageButton_routepoint);
        mapButton = findViewById(R.id.imageButton_map);
        detailButton = findViewById(R.id.imageButton_detail);
        frameLayout = findViewById(R.id.fragment_frame);
        routeAdapter = new RouteAdapter(this);

        changeFragment(Fragments.MAP);



    }

    public void changeFragment(Fragments fragment) {

        Fragment newFragment = null;

        switch (fragment) {
            case MAP:
                newFragment = new MapFragment(map);
                break;
            case DETAIL:
                newFragment = new DetailPointFragment();
                break;
            case POINTS:
                newFragment = new RoutePointsFragment(map);
                break;
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(frameLayout.getId(), newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = Error.generateError(this, getString(R.string.exit_route), getString(R.string.exit_route_description));
        builder.setNegativeButton(getString(R.string.yes), (dialogInterface, i) -> {
            super.onBackPressed();
        });
        builder.setPositiveButton(getString(R.string.no), (dialogInterface, i) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}