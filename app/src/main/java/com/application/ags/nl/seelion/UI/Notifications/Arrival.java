package com.application.ags.nl.seelion.UI.Notifications;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.application.ags.nl.seelion.R;

public class Arrival extends AppCompatActivity {


    private Context acontext;

            public Arrival(Context context){
                this.acontext = context;
            }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrival);
    }


    public void showArrival(){
        final Dialog arrived = new Dialog(acontext);
        arrived.setTitle("Arrived");

        arrived.setContentView(R.layout.activity_arrival);

        Button confirm = (Button) arrived.findViewById(R.id.arrival_confirmBtn);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrived.cancel();
            }
        });

        arrived.show();
    }
}
