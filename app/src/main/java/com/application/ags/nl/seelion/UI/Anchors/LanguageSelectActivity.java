package com.application.ags.nl.seelion.UI.Anchors;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.application.ags.nl.seelion.R;
import com.application.ags.nl.seelion.UI.Notifications.Arrival;
import com.application.ags.nl.seelion.UI.Notifications.Deviation;
import com.application.ags.nl.seelion.UI.popups.Error;
import com.application.ags.nl.seelion.UI.popups.Help;

public class LanguageSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_language_select);

        Button testError = (Button) findViewById(R.id.langSelect_errorBtn);

        testError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Arrival arrival = new Arrival(LanguageSelectActivity.this);
                arrival.showArrival();
            }
        });

        Button testHelp = (Button) findViewById(R.id.langSelect_helpBtn);
        testHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Deviation deviation = new Deviation(LanguageSelectActivity.this);
                deviation.showDeviation();
            }
        });
    }


}
