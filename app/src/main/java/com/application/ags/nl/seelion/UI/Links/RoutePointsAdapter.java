package com.application.ags.nl.seelion.UI.Links;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.ags.nl.seelion.Data.PointOfInterest;
import com.application.ags.nl.seelion.Logic.Map;
import com.application.ags.nl.seelion.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class RoutePointsAdapter extends RecyclerView.Adapter<RoutePointsAdapter.CustomViewHolder> {

    private Context context;
    private List<PointOfInterest> pois;
    private boolean isHistorKm;

    public RoutePointsAdapter(Context context, Map map, boolean isHistorKm) {
        this.context = context;
        this.pois = map.getPois();
        this.isHistorKm = isHistorKm;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_routepoint_rv_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Log.i("test", "ddddd");
        if (pois.get(position).getImages().size() > 0) {
            List<String> urls = pois.get(position).getImages();

            if (urls.size() > 0) {
                if (isHistorKm) {
                    try {
                        holder.imageViewPOI.setImageBitmap(BitmapFactory.decodeStream(context.getAssets().open(urls.get(0))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    Picasso.with(context)
                            .load(urls.get(0))
                            .into(holder.imageViewPOI);
                }
            }
        }
        if (pois.get(position).isVisited()) {
            holder.imageViewState.setImageDrawable(context.getResources().getDrawable(R.drawable.check_icon));
        }else{
            holder.imageViewState.setImageDrawable(context.getResources().getDrawable(R.drawable.uncheck_icon));
        }
        holder.textViewNamePOI.setText(pois.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return pois.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{

        protected TextView textViewNamePOI;
        protected ImageView imageViewPOI;
        protected ImageView imageViewState;

        public CustomViewHolder(View view) {
            super(view);
            this.textViewNamePOI = view.findViewById(R.id.textView_name_poi);
            this.imageViewPOI = view.findViewById(R.id.imageView_image_poi);
            this.imageViewState = view.findViewById(R.id.imageView_state);
        }
    }
}