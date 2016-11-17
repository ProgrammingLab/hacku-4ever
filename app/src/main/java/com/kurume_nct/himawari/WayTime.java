package com.kurume_nct.himawari;

/**
 * Created by prolab on 11/17/16.
 */

public class WayTime {
    private int time;
    private double distance;

    public WayTime(){

    }

    public WayTime(int time){
        this.time = time;
    }

    public WayTime(int time, double distance){
        this.time = time;
        this.distance = distance;
    }

    public void setTime(int time){this.time = time;}
    public void setDistance(double distance){this.distance = distance;}

    public int getTime(){return this.time;}
    public double getDistance(){return this.distance;}

}
