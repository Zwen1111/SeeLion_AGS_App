package com.application.ags.nl.seelion.UI.Links;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.application.ags.nl.seelion.R;
import com.application.ags.nl.seelion.UI.Anchors.DetailPointFragment;
import com.application.ags.nl.seelion.UI.Anchors.RouteActivity;
import com.application.ags.nl.seelion.UI.Anchors.RoutePointsFragment;
import com.google.android.gms.maps.MapFragment;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class RouteAdapter implements View.OnClickListener {

    private int darkButtonColor;
    private int lightButtonColor;
    private RouteActivity routeActivity;

    public RouteAdapter(RouteActivity routeActivity) {
        this.routeActivity = routeActivity;

        darkButtonColor = ContextCompat.getColor(routeActivity, R.color.common_google_signin_btn_text_dark_disabled);
        lightButtonColor = ContextCompat.getColor(routeActivity, R.color.imageButtonColor);

        routeActivity.routePointButton.setOnClickListener(this);
        routeActivity.mapButton.setOnClickListener(this);
        routeActivity.detailButton.setOnClickListener(this);

        changeFragment(new MapFragment());
    }

    private void openRoutePointFragment() {
        changeFragment(new RoutePointsFragment());
    }

    private void openMapFragment() {
        changeFragment(new MapFragment());
    }

    private void openDetailFragment() {
        changeFragment(new DetailPointFragment());
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = routeActivity.getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_frame, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        Log.i("Test", "Messagge");
        switch (view.getId()) {
            case R.id.imageButton_routepoint:
                routeActivity.routePointButton.setBackgroundColor(darkButtonColor);
                routeActivity.mapButton.setBackgroundColor(lightButtonColor);
                routeActivity.detailButton.setBackgroundColor(lightButtonColor);
                Log.i("Test", "Route");
                openRoutePointFragment();
                break;
            case R.id.imageButton_map:
                routeActivity.routePointButton.setBackgroundColor(lightButtonColor);
                routeActivity.mapButton.setBackgroundColor(darkButtonColor);
                routeActivity.detailButton.setBackgroundColor(lightButtonColor);
                Log.i("Test", "Map");
                openMapFragment();
                break;
            case R.id.imageButton_detail:
                routeActivity.routePointButton.setBackgroundColor(lightButtonColor);
                routeActivity.mapButton.setBackgroundColor(lightButtonColor);
                routeActivity.detailButton.setBackgroundColor(darkButtonColor);
                Log.i("Test", "Detail");
                openDetailFragment();
                break;
        }
    }


}