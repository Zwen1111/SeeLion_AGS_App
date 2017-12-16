package com.application.ags.nl.seelion.UI.Links;

import android.content.Context;
import android.database.DataSetObserver;
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

import java.util.List;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class RoutePointsAdapter extends RecyclerView.Adapter<RoutePointsAdapter.CustomViewHolder> {

    private Context context;
    private List<PointOfInterest> pois;

    public RoutePointsAdapter(Context context, Map map) {
        this.context = context;
        this.pois = map.getPois();
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
        // holder.imageViewPOI.setImageBitmap(null); //Todo add real image
        holder.imageViewState.setImageDrawable(context.getResources().getDrawable(R.drawable.check_icon)); //Or R.drawable.uncheck_icon
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