package com.application.ags.nl.seelion.UI.Anchors;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.ags.nl.seelion.Data.PointOfInterest;
import com.application.ags.nl.seelion.R;
import com.application.ags.nl.seelion.UI.Links.DetailPointAdapter;

/**
 * Created by zwen1 on 12/10/2017.
 */

@SuppressLint("ValidFragment")
public class DetailPointFragment extends Fragment {

    public ImageView imageViewPOI;
    public TextView textViewPOI;
    private DetailPointAdapter detailPointAdapter;
    private PointOfInterest pointOfInterest;

    public DetailPointFragment(PointOfInterest pointOfInterest) {
        this.pointOfInterest = pointOfInterest;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailpoint, container, false);
        imageViewPOI = view.findViewById(R.id.imageView_poi_info);
        textViewPOI = view.findViewById(R.id.textView_poi_info);

        textViewPOI.setMovementMethod(new ScrollingMovementMethod());

        detailPointAdapter = new DetailPointAdapter(this);

        if (pointOfInterest != null){
            Drawable d = pointOfInterest.getImageDrawables(getActivity()).get(0);
            if (d != null) {
                imageViewPOI.setImageDrawable(d);
            }
            textViewPOI.setText(pointOfInterest.getDescription());
        }
        return view;
    }

    public PointOfInterest getPointOfInterest() {
        return pointOfInterest;
    }
}