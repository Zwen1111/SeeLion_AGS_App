package com.application.ags.nl.seelion.UI.Anchors;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.application.ags.nl.seelion.Data.Constants;
import com.application.ags.nl.seelion.Logic.Map;
import com.application.ags.nl.seelion.R;

public class RouteSelectActivity extends AppCompatActivity {

    public Spinner routeSpinner;
    private String currentRoute;
    private Button confirmButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_select);
        routeSpinner = findViewById(R.id.spinner_Route);
        String[] spinnerArray = new String[]{Constants.BlindWalls, Constants.HistorKm};
        ArrayAdapter<String> spinnerApdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        routeSpinner.setAdapter(spinnerApdapter);

        currentRoute = Constants.BlindWalls;

        confirmButton = findViewById(R.id.confirm_Button);
        confirmButton.setOnClickListener(view -> {
            SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(Constants.CURRENT_ROUTE, currentRoute);
            editor.commit();

            Intent intent = new Intent(getApplicationContext(), RouteActivity.class);
            intent.putExtra("MAP", currentRoute);
            startActivity(intent);
        });
    }

}
