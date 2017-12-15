package com.application.ags.nl.seelion.Logic;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.application.ags.nl.seelion.Data.HistorKmDataGet;
import com.application.ags.nl.seelion.Data.PointOfInterest;
import com.application.ags.nl.seelion.Data.SqlConnect;
import com.application.ags.nl.seelion.UI.Anchors.MapFragment;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class Map implements Parcelable{

    private List<PointOfInterest> pois;
    private PolylineOptions polylineOptions;

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
        //routeCalculation = new RouteCalculation(this);
    }

    protected Map(Parcel in){
        pois = new ArrayList<>();
        in.readList(this.pois, PointOfInterest.class.getClassLoader());
    }

    public List<PointOfInterest> getPois() {
        return pois;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(this.pois);
    }

    public static final Creator<Map> CREATOR = new Creator<Map>() {
        @Override
        public Map createFromParcel(Parcel source) {
            return new Map(source);
        }

        @Override
        public Map[] newArray(int size) {
            return new Map[size];
        }
    };
}
