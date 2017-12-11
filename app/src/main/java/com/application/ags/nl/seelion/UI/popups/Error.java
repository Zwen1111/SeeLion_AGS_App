package com.application.ags.nl.seelion.UI.popups;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.application.ags.nl.seelion.R;

public class Error extends AppCompatActivity {

    private Context econtext;

    public Error(Context context){
        this.econtext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
    }

    public void showError(){
        final Dialog error = new Dialog(econtext);
        error.setTitle("Error");

        error.setContentView(R.layout.activity_error);

        Button ok = (Button) error.findViewById(R.id.error_OkBtn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error.cancel();
            }
        });

        error.show();
    }
}
