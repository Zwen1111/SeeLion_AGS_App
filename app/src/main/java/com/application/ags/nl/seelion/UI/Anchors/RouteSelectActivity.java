package com.application.ags.nl.seelion.UI.Anchors;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.application.ags.nl.seelion.Data.Constants;
import com.application.ags.nl.seelion.Logic.Map;
import com.application.ags.nl.seelion.Logic.Reset;
import com.application.ags.nl.seelion.R;
import com.application.ags.nl.seelion.UI.popups.Error;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.Locale;

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
        ArrayAdapter<String> spinnerApdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        routeSpinner.setAdapter(spinnerApdapter);
        
        currentRoute = Constants.BlindWalls;

        routeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentRoute = spinnerArray[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        confirmButton = findViewById(R.id.confirm_Button);
        confirmButton.setOnClickListener(view -> {
            boolean connected = false;
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                //we are connected to a network
                connected = true;
            }
            else
                connected = false;
            if (connected) {
                SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(Constants.CURRENT_ROUTE, currentRoute);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), RouteActivity.class);
                intent.putExtra("MAP", currentRoute);
                startActivity(intent);
            }else{
                AlertDialog.Builder builder = Error.generateError(this, getString(R.string.internet_connection_failed), getString(R.string.internet_connection_failed_description));
                builder.setNeutralButton(getString(R.string.ok), (dialogInterface, i) -> {

                });

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
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
                return true;
            case R.id.reset:
                new Reset(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
