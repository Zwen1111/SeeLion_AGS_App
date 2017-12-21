package com.application.ags.nl.seelion.UI.Anchors;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.ags.nl.seelion.Data.Constants;
import com.application.ags.nl.seelion.Data.PointOfInterest;
import com.application.ags.nl.seelion.Logic.Transformation;
import com.application.ags.nl.seelion.R;
import com.application.ags.nl.seelion.UI.Links.DetailPointAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String currentRoute = sharedPreferences.getString(Constants.CURRENT_ROUTE, null);


        if (pointOfInterest != null) {
            if (currentRoute.equals(Constants.HistorKm)) {
                List<Drawable> drawableList = pointOfInterest.getImageDrawables(getActivity());
                if (drawableList.size() > 0) {
                    Drawable d = drawableList.get(0);
                    if (d != null) {
                        imageViewPOI.setImageDrawable(d);
                    }
                }
            }else{
                List<String> urls = pointOfInterest.getImages();
                if (urls.size() > 0)
                Picasso.with(getActivity())
                        .load(urls.get(0))
                        .into(imageViewPOI);
            }

            String description = pointOfInterest.getDescription();
            if (description.length() > 0) {
                textViewPOI.setText(description);
            } else {
                textViewPOI.setText(getString(R.string.no_description));
            }
        }
        return view;
    }
}