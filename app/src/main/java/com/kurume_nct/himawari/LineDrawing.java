package com.kurume_nct.himawari;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Created by shouhei on 2016/11/16.
 */

public class LineDrawing {
    private String API_KEY;
    private Boolean isIn;
    GoogleMap map;
    Polyline line;

    public LineDrawing(Context context, GoogleMap map){
        this.map = map;
        this.API_KEY = context.getString(R.string.google_maps_key);
    }
    public void drawRoute(LatLng curr,LatLng dest,final ArrayList<StoreData> waypoints,final int price,final int hour,final int minute,
                          GoogleMap map,DownloadWayTask.CallBackTask callback) {
        if (dest == null) {
            Log.e("fuga", "drawRoute");
            return;
        }

        this.map = map;
        int query = 10;
        while(query != 0) {
            query--;
            String output = "json";
            String parameters = "";
            parameters += "origin=" + curr.latitude + "," + curr.longitude;
            parameters += "&destination=" + dest.latitude + "," + dest.longitude;

            if (!waypoints.isEmpty()) parameters += "&waypoints=optimize:true" + "%7C";
            int cnt = 0;
            int stayedTime = 0;
            int tryPrice = 0;
            Collections.shuffle(waypoints);
            boolean isEnd = false;
            ArrayList<StoreData> stores = new ArrayList<StoreData>();
            for (StoreData waypoint : waypoints) {
                if (cnt == 2) {
                    isEnd = true;
                    break;
                }

                if(query < 3 && cnt == 1){
                    isEnd = true;
                    break;
                }

                if(tryPrice+waypoint.getPrice() < price){
                    tryPrice += waypoint.getPrice();
                    stores.add(waypoint);
                }
                else{
                    break;
                }
                parameters += waypoint.getLatLng().latitude + "," + waypoint.getLatLng().longitude + "%7C";
                stayedTime += waypoint.getStayedTime();
                ++cnt;
            }
            if(!isEnd)continue;

            parameters = parameters.substring(0, parameters.length() - 3);
            parameters += "&mode=walking";
            Calendar calendar = Calendar.getInstance();
            long unixTime = calendar.getTimeInMillis();
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            int m = calendar.get(Calendar.MINUTE);
            int p = 0;
            if(m > minute){
                p = (hour - h -1)*3600 + (60 - (m - minute))*60;
            }
            else{
                p = (hour - h)*3600 + (minute - m)*60;
            }
            if(p-stayedTime < 0){
                continue;
            }
            parameters += "&arrival_time=" + String.valueOf(unixTime + 1000*(p - stayedTime));

            //ここにAPIキーを追加してください
            parameters += "&key=" + API_KEY;

            String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
            DownloadWayTask task = new DownloadWayTask(map, line, stores);
            task.setOnCallBack(callback);
            task.execute(url);
            break;
        }
    }
}
