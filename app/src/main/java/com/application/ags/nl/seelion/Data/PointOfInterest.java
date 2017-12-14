package com.application.ags.nl.seelion.Data;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class PointOfInterest {

    private String title;
    private String description;
    private LatLng location;

    public PointOfInterest(String title, String description, LatLng location){
        this.title = title;
        this.description = description;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LatLng getLocation() {
        return location;
    }
}