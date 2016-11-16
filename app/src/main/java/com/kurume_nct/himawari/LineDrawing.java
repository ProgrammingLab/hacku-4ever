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
    public void drawRoute(LatLng curr,LatLng dest,List<LatLng> waypoints) {
        if (dest == null) {
            Log.e("fuga", "drawRoute");
            return;
        }

        String output = "json";

        String parameters = "";
        parameters += "origin=" + curr.latitude + "," + curr.longitude;
        parameters += "&destination=" + dest.latitude + "," + dest.longitude;

        if (!waypoints.isEmpty()) parameters += "&waypoints=optimize:true";
        for (LatLng waypoint : waypoints) {
            parameters += "|" + waypoint.latitude + "," + waypoint.longitude;
        }

        parameters += "&mode=walking";

        //ここにAPIキーを追加してください
        parameters += "&key="+API_KEY;

        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        DownLoadTask task = new DownLoadTask();
        task.execute(url);
    }

    private class DownLoadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... url) {
            String overview_polyline= "";
            ObjectMapper mapper = new ObjectMapper();

            JsonNode rootNode = null;
            try {
                Log.d("test",url[0]);
                rootNode = mapper.readTree(new URL(url[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }

            overview_polyline = rootNode.get("routes").get(0).get("overview_polyline").get("points").asText();

            return overview_polyline;
        }
        @Override
        protected void onPostExecute(String overview_polyline){
            super.onPostExecute(overview_polyline);

            if(line != null) line.remove();
            List<LatLng> routes = PolyUtil.decode(overview_polyline + "");

            line = map.addPolyline(new PolylineOptions()
                    .addAll(routes)
                    .width(10)
                    .color(Color.BLUE));

            return;
        }
    }
}
