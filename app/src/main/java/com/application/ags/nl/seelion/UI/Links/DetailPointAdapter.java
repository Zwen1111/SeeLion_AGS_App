package com.application.ags.nl.seelion.UI.Links;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.application.ags.nl.seelion.UI.Anchors.DetailPointFragment;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class DetailPointAdapter  {

    private DetailPointFragment detailPointFragment;

    public DetailPointAdapter(DetailPointFragment detailPointFragment) {
        this.detailPointFragment = detailPointFragment;
        //Todo Get data from sql request class and add data to imageview and textview
    }
}
