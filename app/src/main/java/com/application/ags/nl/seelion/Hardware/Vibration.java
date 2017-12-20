package com.application.ags.nl.seelion.Hardware;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by zwen1 on 20-12-2017.
 */

public class Vibration {

    public Vibration(Context context){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(1000);
    }
}
