package com.application.ags.nl.seelion.UI.Anchors;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.application.ags.nl.seelion.Logic.Map;
import com.application.ags.nl.seelion.Logic.RouteCalculation;
import com.application.ags.nl.seelion.R;
import com.application.ags.nl.seelion.UI.Links.RouteAdapter;
import com.application.ags.nl.seelion.UI.popups.Error;

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

        map = getIntent().getParcelableExtra("MAP");

        routePointButton = findViewById(R.id.imageButton_routepoint);
        mapButton = findViewById(R.id.imageButton_map);
        detailButton = findViewById(R.id.imageButton_detail);
        frameLayout = findViewById(R.id.fragment_frame);
        routeAdapter = new RouteAdapter(this);


//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(frameLayout.getId(), new MapFragment());
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.commit();

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