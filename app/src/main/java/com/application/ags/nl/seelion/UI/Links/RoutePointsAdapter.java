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

import com.application.ags.nl.seelion.R;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class RoutePointsAdapter extends RecyclerView.Adapter<RoutePointsAdapter.CustomViewHolder> {

    //Todo add list variable here and init it in de constructor
    private Context context;


    public RoutePointsAdapter(Context context) {
        this.context = context;
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
        // holder.textViewNamePOI.setText(R.string.app_name); //Todo add real text
    }

    @Override
    public int getItemCount() {
        //Todo add list count with poi items
        return 1;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{

        protected ImageView imageViewPOI;
        protected ImageView imageViewState;
        protected TextView textViewNamePOI;

        public CustomViewHolder(View view) {
            super(view);
            this.imageViewPOI = view.findViewById(R.id.imageView_image_poi);
            this.imageViewState = view.findViewById(R.id.imageView_state);
            this.textViewNamePOI = view.findViewById(R.id.textView_poi_info);
        }
    }
}