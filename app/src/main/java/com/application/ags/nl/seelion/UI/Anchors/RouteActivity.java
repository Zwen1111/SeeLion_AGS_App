package com.application.ags.nl.seelion.UI.Anchors;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.application.ags.nl.seelion.Data.Constants;
import com.application.ags.nl.seelion.Data.PointOfInterest;
import com.application.ags.nl.seelion.Logic.Map;
import com.application.ags.nl.seelion.Logic.SqlRequest;
import com.application.ags.nl.seelion.R;
import com.application.ags.nl.seelion.UI.Links.RouteAdapter;
import com.application.ags.nl.seelion.UI.popups.Error;

import static com.application.ags.nl.seelion.UI.Anchors.LanguageSelectActivity.sqlConnect;


public class RouteActivity extends AppCompatActivity {

    public ImageButton routePointButton;
    public ImageButton mapButton;
    public ImageButton detailButton;
    public FrameLayout frameLayout;
    private RouteAdapter routeAdapter;
    private PointOfInterest currentPOI;
    private Fragment mapFragment = null;
    private boolean done;

    public enum Fragments {
        MAP, DETAIL, POINTS
    }

    private Map map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Current POI", null);
        editor.commit();

        String mapString = getIntent().getStringExtra("MAP");

        switch (mapString) {
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
                newFragment = new RoutePointsFragment(map);
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

    public void setCurrentPOI(PointOfInterest currentPOI){
        this.currentPOI = currentPOI;
        changeFragment(Fragments.DETAIL);
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
}