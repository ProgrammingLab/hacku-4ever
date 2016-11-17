package com.kurume_nct.himawari;


import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadWayTask  extends AsyncTask<String,Void,Pair<List<WayTime>,String>> {
    private CallBackTask callbacktask;
    private GoogleMap map;
    private Polyline line;

    public DownloadWayTask(){

    }

    public DownloadWayTask(GoogleMap map,Polyline line){
        this.map = map;
        this.line = line;
    }

    @Override
    protected Pair<List<WayTime>,String> doInBackground(String... url){
        String overview_polyline= "";
        ObjectMapper mapper = new ObjectMapper();

        JsonNode rootNode = null;
        List <WayTime> list = new ArrayList<WayTime>();
        try {
            Log.d("test",url[0]);
            rootNode = mapper.readTree(new URL(url[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }

        overview_polyline = rootNode.get("routes").get(0).get("overview_polyline").get("points").asText();
        JsonNode n = rootNode.get("routes").get(0).get("legs");

        for(JsonNode m : n){
            list.add(new WayTime(m.get("duration").get("value").asInt()));
        }
        return Pair.create(list,overview_polyline);
    }

    @Override
    protected void onPostExecute(Pair<List<WayTime>,String> result){
        super.onPostExecute(result);
        List<LatLng> routes = PolyUtil.decode(result.second + "");

        line = map.addPolyline(new PolylineOptions()
                .addAll(routes)
                .width(10)
                .color(Color.BLUE));
        callbacktask.CallBack(result.first);
    }

    public void setOnCallBack(CallBackTask _cbj) {
        callbacktask = _cbj;
    }

    public static class CallBackTask {
        public void CallBack(List<WayTime> result) {
        }
    }
}
