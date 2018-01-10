package com.application.ags.nl.seelion.UI.Anchors;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.ags.nl.seelion.Logic.Map;
import com.application.ags.nl.seelion.R;
import com.application.ags.nl.seelion.UI.Links.RoutePointsAdapter;

/**
 * Created by zwen1 on 12/10/2017.
 */

@SuppressLint("ValidFragment")
public class RoutePointsFragment extends Fragment {

    private RecyclerView recyclerView;
    private Map map;
    private boolean isHistorKm;
    private RouteActivity routeActivity;

    public RoutePointsFragment(RouteActivity routeActivity, Map map, boolean isHistorKm){
        this.map = map;
        this.isHistorKm = isHistorKm;
        this.routeActivity = routeActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routepoint, container, false);

        recyclerView = view.findViewById(R.id.recycleView_poi);
        recyclerView.setAdapter(new RoutePointsAdapter(routeActivity, getActivity(), map, isHistorKm));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        return view;
    }
}