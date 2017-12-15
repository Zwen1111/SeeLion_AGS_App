package com.application.ags.nl.seelion.UI.Anchors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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
        String[] spinnerArray = new String[]{"Blindwalls", "Historischekilometer"};
        ArrayAdapter<String> spinnerApdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        routeSpinner.setAdapter(spinnerApdapter);
        routeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String route = routeSpinner.getItemAtPosition(i).toString();
                if(currentRoute != route) {
                    switch (route) {
                        case "Blindwalls":
                            currentRoute = "Blindwalls";
                            break;
                        case "Historischekilometer":
                            currentRoute = "Historischekilometer";
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        confirmButton = findViewById(R.id.confirm_Button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentRoute == "Historischekilometer"){
                    Map map = Map.generateHistorKmMap(getApplicationContext());
                    Intent intent  = new Intent(getApplicationContext(), RouteActivity.class);
                    intent.putExtra("MAP", map);
                    startActivity(intent);
                }
            }
        });
    }
}
