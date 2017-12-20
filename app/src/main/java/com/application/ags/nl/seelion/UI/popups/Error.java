package com.application.ags.nl.seelion.UI.popups;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.application.ags.nl.seelion.Hardware.Vibration;
import com.application.ags.nl.seelion.R;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class Error {

    public static AlertDialog.Builder generateError(Context context, String title, String message){
        AlertDialog.Builder builer = new AlertDialog.Builder(context);
        builer.setMessage(message);
        builer.setTitle(title);
        new Vibration(context);
        return builer;
    }
}
