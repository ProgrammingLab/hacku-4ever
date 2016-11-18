package com.kurume_nct.himawari;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by prolab on 11/17/16.
 */

public class WayTime implements Parcelable {
    private int time;
    private int distance;

    public WayTime(){

    }

    public WayTime(int time){
        this.time = time;
    }

    public WayTime(int time, int distance){
        this.time = time;
        this.distance = distance;
    }

    protected WayTime(Parcel in) {
        time = in.readInt();
        distance = in.readInt();
    }

    public static final Creator<WayTime> CREATOR = new Creator<WayTime>() {
        @Override
        public WayTime createFromParcel(Parcel in) {
            return new WayTime(in);
        }

        @Override
        public WayTime[] newArray(int size) {
            return new WayTime[size];
        }
    };

    public void setTime(int time){this.time = time;}
    public void setDistance(int distance){this.distance = distance;}

    public int getTime(){return this.time;}
    public int getDistance(){return this.distance;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(time);
        parcel.writeInt(distance);
    }
}
