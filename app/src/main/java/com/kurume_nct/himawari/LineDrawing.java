package com.kurume_nct.himawari;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by shouhei on 2016/11/16.
 */

public class LineDrawing {
    private GoogleMap map;
    private Polyline line;
    private String API_KEY;

    public LineDrawing(Context context, GoogleMap map){
        this.map = map;
        this.API_KEY = context.getString(R.string.google_maps_key);
    }
    public void drawRoute(LatLng curr,LatLng dest,final List<StoreData> waypoints,GoogleMap map,DownloadWayTask.CallBackTask callback) {
        if (dest == null) {
            Log.e("fuga", "drawRoute");
            return;
        }

        this.map = map;

        String output = "json";

        String parameters = "";
        parameters += "origin=" + curr.latitude + "," + curr.longitude;
        parameters += "&destination=" + dest.latitude + "," + dest.longitude;

        if (!waypoints.isEmpty()) parameters += "&waypoints=optimize:true" + "%7C";
        int cnt = 0;
        for (StoreData waypoint : waypoints) {
            if(cnt == 2){
                break;
            }
            parameters += waypoint.getLatLng().latitude + "," + waypoint.getLatLng().longitude + "%7C";
            ++cnt;
        }
        parameters = parameters.substring(0,parameters.length()-3);
        parameters += "&mode=walking";

        //ここにAPIキーを追加してください
        parameters += "&key="+API_KEY;

        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        Log.d("HOGE",url);
        DownloadWayTask task = new DownloadWayTask(map,line);
        task.setOnCallBack(callback);
        task.execute(url);
    }
}
