package com.application.ags.nl.seelion.UI.Notifications;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.application.ags.nl.seelion.R;

public class Deviation extends AppCompatActivity {

    private Context dcontext;

    public Deviation(Context context){
        this.dcontext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deviation);
    }


    public void showDeviation(){
        final Dialog deviation = new Dialog(dcontext);
        deviation.setTitle("Deviated");

        deviation.setContentView(R.layout.activity_deviation);

        Button confirm = (Button) deviation.findViewById(R.id.deviation_confirmBtn);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deviation.cancel();
            }
        });

        deviation.show();
    }
}
