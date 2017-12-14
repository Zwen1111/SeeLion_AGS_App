package com.application.ags.nl.seelion.UI.Anchors;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import com.application.ags.nl.seelion.R;
import com.application.ags.nl.seelion.UI.Links.RouteAdapter;
import com.google.android.gms.maps.MapFragment;

public class RouteActivity extends AppCompatActivity {

    public ImageButton routePointButton;
    public ImageButton mapButton;
    public ImageButton detailButton;
    private RouteAdapter routeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        routePointButton = findViewById(R.id.imageButton_routepoint);
        mapButton = findViewById(R.id.imageButton_map);
        detailButton = findViewById(R.id.imageButton_detail);

        routeAdapter = new RouteAdapter(this);

    }

}