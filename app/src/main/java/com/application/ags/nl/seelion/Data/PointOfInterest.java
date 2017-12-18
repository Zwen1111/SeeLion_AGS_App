package com.application.ags.nl.seelion.Data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwen1 on 12/10/2017.
 */

public class PointOfInterest implements Parcelable {

    private int id;
    private String title;
    private String description;
    private double longitude;
    private double latitude;
    private List<String> images;
    private boolean visited;

    public PointOfInterest(int id, String title, String description, LatLng location, List<String> drawableList){
        this.id = id;
        this.title = title;
        this.description = description;
        this.latitude = location.latitude;
        this.longitude = location.longitude;
        this.images = drawableList;
        this.visited = false;
    }

    protected PointOfInterest(Parcel in){
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        images = new ArrayList<>();
        in.readList(images, String.class.getClassLoader());
        this.visited = false;
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

    public List<String> getImages() {
        return images;
    }

    public String getImagesJsonString(){
        JSONArray array = new JSONArray(images);
        String arrayString = array.toString();
        return arrayString;
    }

    public List<Drawable> getImageDrawables(Context context){
        List<Drawable> drawables = new ArrayList<>();
        for (String image : images) {
            try {
                // get input stream
                InputStream ims = context.getAssets().open(image);
                // load image as Drawable
                Drawable d = Drawable.createFromStream(ims, null);
                drawables.add(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return drawables;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
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
        parcel.writeList(images);
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