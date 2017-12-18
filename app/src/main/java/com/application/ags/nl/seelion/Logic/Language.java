package com.application.ags.nl.seelion.Logic;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class Language {


    private Context context;

    public Language(Context context){
        this.context = context;
    }

    // NEEDS TESTING
    public void changeLanguage(String languageToChangeInto){
        Locale locale = new Locale(languageToChangeInto);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

}
