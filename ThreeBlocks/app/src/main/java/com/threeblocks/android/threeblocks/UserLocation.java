package com.threeblocks.android.threeblocks;

import android.content.Context;

/**
 * Created by seungwoo on 2017-08-10.
 */

public class UserLocation {

    private MapPoint mMapPoint;

    private static UserLocation sUserLocation;
    public static UserLocation getInstance(Context context) {
        if(sUserLocation == null) {
            sUserLocation = new UserLocation(context.getApplicationContext());
        }
        return sUserLocation;
    }
    private UserLocation(Context context) {
        mMapPoint = new MapPoint();
    }

    //get set 함수
    public void setsUserLocation(MapPoint mapPoint) {
        this.mMapPoint = mapPoint;
    }
    public MapPoint getMapPoint(){
        return mMapPoint;
    }

}
