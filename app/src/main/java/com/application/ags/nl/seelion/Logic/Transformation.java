package com.application.ags.nl.seelion.Logic;

import android.graphics.Bitmap;

/**
 * Created by zwen1 on 20-12-2017.
 */

public class Transformation implements com.squareup.picasso.Transformation {

    private int mSize;

    public Transformation(int size){
        mSize = size;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        float scale;
        int newSize;
        Bitmap scaleBitmap;
        scale = (float) mSize / source.getWidth();
        newSize = Math.round(source.getHeight() * scale);
        scaleBitmap = Bitmap.createScaledBitmap(source, mSize, newSize, true);
        if (scaleBitmap != source){
            source.recycle();
        }

        return scaleBitmap;
    }

    @Override
    public String key() {
        return "scaleRespectRatio" + mSize;
    }
}
