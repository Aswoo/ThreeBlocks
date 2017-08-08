package com.threeblocks.android.threeblocks;

/**
 * Created by seungwoo on 2017-08-09.
 */

public class MapPoint {
    private String Name;
    private double latitude;
    private double logitude;


    public MapPoint(){
        super();
    }
    public MapPoint(String Name,double latitude,double logitude) {
        this.Name = Name;
        this.latitude = latitude;
        this.logitude = logitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLogitude() {
        return logitude;
    }

    public void setLogitude(double logitude) {
        this.logitude = logitude;
    }



}
