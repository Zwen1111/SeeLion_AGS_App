package com.application.ags.nl.seelion.UI.Anchors;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.application.ags.nl.seelion.Data.BlindWallsDataGet;
import com.application.ags.nl.seelion.Data.Constants;
import com.application.ags.nl.seelion.Data.HistorKmDataGet;
import com.application.ags.nl.seelion.Data.SqlConnect;
import com.application.ags.nl.seelion.Hardware.Vibration;
import com.application.ags.nl.seelion.Logic.Reset;
import com.application.ags.nl.seelion.Logic.SqlRequest;
import com.application.ags.nl.seelion.R;
import com.application.ags.nl.seelion.UI.popups.Error;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class LanguageSelectActivity extends AppCompatActivity {

    private Spinner languageSpinner;
    private String currentLanguage;
    private Button selectButton;

    public static RequestQueue requestQueue;
    public static SqlConnect sqlConnect;

    /* this class is used for choosing the language, this is the first activity that will be launched.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);

        sqlConnect = new SqlConnect(this);
        requestQueue = Volley.newRequestQueue(this);
        askPermissions();

        if (new SqlRequest().isEmtpy()) {
            new HistorKmDataGet(this);
            new BlindWallsDataGet(this);
        }

        SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        boolean saveExit = settings.getBoolean("Save exit", true);
        if (!saveExit){
            AlertDialog.Builder builder = Error.generateError(this, getString(R.string.not_save_exit_detected), getString(R.string.not_save_exit_detected_description));
            builder.setNegativeButton(getString(R.string.yes), (dialogInterface, i) -> {
                String currentRoute = settings.getString(Constants.CURRENT_ROUTE, null);
                Intent intent = new Intent(getApplicationContext(), RouteActivity.class);
                intent.putExtra("MAP", currentRoute);
                startActivity(intent);
            });
            builder.setPositiveButton(getString(R.string.no), (dialogInterface, i) -> {
                editor.putBoolean("Save exit", true);
                editor.commit();
                new SqlRequest().clearVisitedPois();
                new SqlRequest().clearWalkedLocations();
            });

            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        editor.putFloat("MARKER_COLOR", BitmapDescriptorFactory.HUE_GREEN);
        editor.putInt("WALKED_ROUTE_COLOR", Color.GREEN);
        editor.putInt("ROUTE_COLOR", Color.RED);

        languageSpinner = findViewById(R.id.language_select_activty_select_language_comboBox);
        String[] spinnerArray = new String[]{"English", "Nederlands"};
        ArrayAdapter<String> spinnerApdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        languageSpinner.setAdapter(spinnerApdapter);

        if (settings.getString("Language", "en").equals("en")){
            languageSpinner.setSelection(0);
        }else{
            languageSpinner.setSelection(1);
        }

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String language = languageSpinner.getItemAtPosition(i).toString();
                if (currentLanguage != language) {
                    switch (language) {
                        case "Nederlands":
                            currentLanguage = "nl";
                            break;
                        case "English":
                            currentLanguage = "en";
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        selectButton = findViewById(R.id.language_activty_select_button);
        selectButton.setOnClickListener(view -> {
            editor.putString("Language", currentLanguage);
            editor.commit();

            setLocale(currentLanguage);
            Intent i  = new Intent(getApplicationContext(), RouteSelectActivity.class);
            startActivity(i);
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

    private void setLocale(String language)
    {
       Locale myLocale = new Locale(language);
       Resources res = getResources();
       DisplayMetrics dm = res.getDisplayMetrics();
       Configuration conf = res.getConfiguration();
       conf.locale = myLocale;
       res.updateConfiguration(conf, dm);
    }

    private void askPermissions(){
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
        }else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, 42);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean granted = true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED && granted){
                granted = false;
            }
        }
        if (!granted){
            AlertDialog.Builder builder = Error.generateError(this, getString(R.string.permissions_denied), getString(R.string.permissions_denied_description));
            builder.setNegativeButton("Nee", (dialogInterface, i) -> {
                System.exit(0);
            });
            builder.setPositiveButton("Ja", (dialogInterface, i) -> {
                askPermissions();
            });

            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }
}
