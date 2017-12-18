package com.application.ags.nl.seelion.UI.Links;

import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.application.ags.nl.seelion.Data.PointOfInterest;
import com.application.ags.nl.seelion.UI.Anchors.DetailPointFragment;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class DetailPointAdapter  {

    private DetailPointFragment detailPointFragment;

    public DetailPointAdapter(DetailPointFragment detailPointFragment) {
        this.detailPointFragment = detailPointFragment;
        //todo set text and image but it gives me an error
    }
}