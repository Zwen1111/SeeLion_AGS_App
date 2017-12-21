package com.application.ags.nl.seelion.Logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;

import com.application.ags.nl.seelion.Data.Constants;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.Locale;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class Reset {

    public Reset(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putFloat("MARKER_COLOR", BitmapDescriptorFactory.HUE_GREEN);
        editor.putInt("WALKED_ROUTE_COLOR", Color.GREEN);
        editor.putBoolean("Save exit", true);
        editor.commit();

        Locale myLocale = new Locale("en");
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}
