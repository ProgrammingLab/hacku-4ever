package com.kurume_nct.himawari;


import android.content.Context;
import android.location.Location;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


public class SearchingStores{
    private LatLng latLng;
    private String API_KEY;
    private final int radius = 1000;
    private final String[] storeTypes = {"amusement_park","aquarium","art_gallery",
            "book_store","bowling_alley","cafe","church", "department_store",
            "library","movie_theater","museum","park", "restaurant",
            "shopping_mall","spa","zoo"};

    public SearchingStores(){

    }
    public SearchingStores(Context context,Location location){
        API_KEY = context.getString(R.string.google_maps_key);
        this.latLng = new LatLng(location.getLatitude(),location.getLongitude());
    }

    public SearchingStores(Context context, LatLng latLng){
        API_KEY = context.getString(R.string.google_maps_key);
        this.latLng = latLng;
    }

    public SearchingStores(String key, LatLng latLng){
        API_KEY = key;
        this.latLng = latLng;
    }

    public SearchingStores(String key, Location location){
        API_KEY = key;
        this.latLng = new LatLng(location.getLatitude(),location.getLongitude());
    }

    public void getParsedData(DownloadTask.CallBackTask callback){
        try {
            URL url = this.setURL();
            DownloadTask task = new DownloadTask();
            task.setOnCallBack(callback);
            task.execute(url);
        }catch (NullPointerException e){
            Log.e("location",e.getMessage());
        }
    }

    @Nullable
    private URL setURL(){
        StringBuilder urlBuilder =  new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json");
        urlBuilder.append("?location=" + this.latLng.latitude + "," + this.latLng.longitude);
        urlBuilder.append("&radius="+this.radius);
        urlBuilder.append("&language=ja");
        urlBuilder.append("&types=" + this.createType());
        urlBuilder.append("&key="+API_KEY);
        try {
            URI uri = new URI(urlBuilder.toString());
            return uri.toURL();
        }
        catch (URISyntaxException e){
            Log.e("location",e.getMessage());
        }
        catch (MalformedURLException e){
            Log.e("location",e.getMessage());
        }

        return null;
    }

    private String createType(){
        String places = "";
        for (String tmp: storeTypes) {
            places += tmp + "%7C";
        }
        places = places.substring(0,places.length()-3);
        return places;
    }
}
