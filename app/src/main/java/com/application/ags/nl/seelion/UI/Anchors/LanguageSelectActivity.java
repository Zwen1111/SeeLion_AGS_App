package com.application.ags.nl.seelion.UI.Anchors;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.application.ags.nl.seelion.Data.BlindWallsDataGet;
import com.application.ags.nl.seelion.R;
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
                Error errorShow = new Error(LanguageSelectActivity.this);
                errorShow.showError();
            }
        });

        Button testHelp = (Button) findViewById(R.id.langSelect_helpBtn);
        testHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Help help = new Help(LanguageSelectActivity.this);
                help.showHelp();
            }
        });
    }


}
