package com.application.ags.nl.seelion.UI.Anchors;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.application.ags.nl.seelion.R;

import java.util.Locale;

public class LanguageSelectActivity extends AppCompatActivity {

    private Spinner languageSpinner;
    private String currentLanguage;
    private Button selectButton;

    /* this class is used for choosing the language, this is the first activity that will be launched.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);
        languageSpinner = findViewById(R.id.language_select_activty_select_language_comboBox);
        String[] spinnerArray = new String[]{"english", "nederlands"};
        ArrayAdapter<String> spinnerApdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        languageSpinner.setAdapter(spinnerApdapter);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String language = languageSpinner.getItemAtPosition(i).toString();
                if(currentLanguage != language) {
                    switch (language) {
                        case "nederlands":
                            currentLanguage = "nl";
                            break;
                        case "english":
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
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale(currentLanguage);
                Intent i  = new Intent(getApplicationContext(), RouteSelectActivity.class);
                startActivity(i);
            }
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
}
