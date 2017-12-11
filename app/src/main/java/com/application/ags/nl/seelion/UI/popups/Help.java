package com.application.ags.nl.seelion.UI.popups;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.application.ags.nl.seelion.R;

public class Help extends AppCompatActivity {

    private Context hcontext;

    public Help(Context context){
        this.hcontext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    public void showHelp(){
        final Dialog help = new Dialog(hcontext);
        help.setTitle("Help");

        help.setContentView(R.layout.activity_help);

        Button ok = (Button) help.findViewById(R.id.help_OkBtn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                help.cancel();
            }
        });

        help.show();
    }
}
