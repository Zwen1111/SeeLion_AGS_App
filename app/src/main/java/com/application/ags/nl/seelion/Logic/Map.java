package com.application.ags.nl.seelion.Logic;

import android.content.Context;

import com.application.ags.nl.seelion.Data.HistorKmDataGet;
import com.application.ags.nl.seelion.Data.PointOfInterest;
import com.application.ags.nl.seelion.Data.SqlConnect;
import com.application.ags.nl.seelion.UI.Anchors.MapFragment;

import java.util.List;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class Map {

    private List<PointOfInterest> pois;

    public static Map generateHistorKmMap(Context context){
        HistorKmDataGet historKmDataGet = new HistorKmDataGet(context);

        Map map = new Map(new SqlRequest().getHistorKmPois());

        return map;
    }

    public static Map generateBlindWallsMap(){
        Map map = new Map(new SqlRequest().getBlindWallsPois());

        return map;
    }

    private Map(List<PointOfInterest> pois){
        this.pois = pois;


    }
}
