package com.application.ags.nl.seelion.UI.Anchors;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.application.ags.nl.seelion.Data.Constants;
import com.application.ags.nl.seelion.Data.SqlConnect;
import com.application.ags.nl.seelion.R;
import com.application.ags.nl.seelion.UI.popups.Error;

import java.util.Locale;

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
        while(!arePermissionsGiven());
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
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
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Save exit", true);
                editor.commit();
            });

            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

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
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("Language", currentLanguage);
            editor.commit();

            setLocale(currentLanguage);
            Intent i  = new Intent(getApplicationContext(), RouteSelectActivity.class);
            startActivity(i);
        });
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

    private boolean arePermissionsGiven(){
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, 42);
            return false;
            }
    }
}
