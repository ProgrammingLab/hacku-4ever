package com.kurume_nct.himawari;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadTask extends AsyncTask<URL,Void,ArrayList<StoreData>> {
    private CallBackTask callbacktask;

    @Override
    protected ArrayList<StoreData> doInBackground(URL... url) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(url[0]);
        } catch (IOException e) {
            Log.e("asynctask",e.getMessage());
        }

        ArrayList<StoreData> list = new ArrayList<StoreData>();
        for(JsonNode n : rootNode.get("results")){
            LatLng latLng = new LatLng(n.get("geometry").get("location").get("lat").asDouble(),
                    n.get("geometry").get("location").get("lng").asDouble());
            String name = n.get("name").asText();
            List<String> types = new ArrayList<String>();
            for(JsonNode m : n.get("types")){
                types.add(m.asText());
            }
            list.add(new StoreData(latLng,name,types));
        }
        return list;
    }
    @Override
    protected void onPostExecute(ArrayList<StoreData> result) {
        super.onPostExecute(result);
        callbacktask.CallBack(result);
    }

    public void setOnCallBack(CallBackTask _cbj) {
        callbacktask = _cbj;
    }

    public static class CallBackTask {
        public void CallBack(ArrayList<StoreData> result) {
        }
    }
}