package com.application.ags.nl.seelion.Hardware;
import android.app.Activity;
import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
/**
 * Created by zwen1 on 12/10/2017.
 */

public class Vibration {

    Vibrator v;

    public Vibration(Activity activity){
        v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);

        // may crash, deprecated method, newer method only from api 26, this is min api 15
        if(v != null){ v.vibrate(200);}
    }
}
