package com.application.ags.nl.seelion.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class PointOfInterest implements Parcelable {

    private int id;
    private String title;
    private String description;
    private double longitude;
    private double latitude;

    public PointOfInterest(int id, String title, String description, LatLng location){
        this.id = id;
        this.title = title;
        this.description = description;
        this.latitude = location.latitude;
        this.longitude = location.longitude;
    }

    protected PointOfInterest(Parcel in){
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LatLng getLocation() {
        return new LatLng(latitude, longitude);
    }

    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
    }

    public static final Creator<PointOfInterest> CREATOR = new Creator<PointOfInterest>() {
        @Override
        public PointOfInterest createFromParcel(Parcel source) {
            return new PointOfInterest(source);
        }

        @Override
        public PointOfInterest[] newArray(int size) {
            return new PointOfInterest[size];
        }
    };
}