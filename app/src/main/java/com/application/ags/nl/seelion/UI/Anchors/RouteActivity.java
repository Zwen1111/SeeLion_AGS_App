package com.application.ags.nl.seelion.UI.Anchors;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import com.application.ags.nl.seelion.R;
import com.application.ags.nl.seelion.UI.Links.RouteAdapter;
import com.application.ags.nl.seelion.UI.popups.Error;


public class RouteActivity extends AppCompatActivity {

    public ImageButton routePointButton;
    public ImageButton mapButton;
    public ImageButton detailButton;
    public FrameLayout frameLayout;
    private RouteAdapter routeAdapter;
    private PointOfInterest currentPOI;

    public enum Fragments {
        MAP, DETAIL, POINTS
    }

    private Map map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        SharedPreferences settings = getSharedPreferences("SeeLion", 0);
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
                newFragment = new MapFragment(this, map);
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