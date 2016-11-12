package com.kurume_nct.himawari;


import android.content.Context;
import android.location.Location;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class SearchingStores{
    private Location nowLocation = null;
    private String API_KEY;
    private final String[] storeTypes = {"amusement_park","aquarium","art_gallery",
            "book_store","bowling_alley","cafe","church","clothing_store",
            "department_store","food","library","movie_theater","museum","park",
            "restaurant","shopping_mall","spa","zoo"};


    public SearchingStores(){

    }
    public SearchingStores(Context context,Location location){
        API_KEY = context.getString(R.string.google_maps_key);
        this.nowLocation = location;
    }

    public void getJSONData(){
        double lat=0.0,lng=0.0;
        try {
            lat = this.nowLocation.getLatitude();
            lng = this.nowLocation.getLongitude();
        }catch (NullPointerException e){
            Log.e("location",e.getMessage());
        }

        URL url = this.setURL(lat,lng);
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            BufferedInputStream is = new BufferedInputStream(con.getInputStream());
            String path = Environment.getExternalStorageDirectory() + "/himawari/";
            String fileName = "result.json";
            File dir = new File(path);
            dir.mkdirs();
            File outputFile = new File(dir, fileName);
            FileOutputStream fos = new FileOutputStream(outputFile);

            int bytesRead = -1;
            byte[] buffer = new byte[1024];
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.flush();
            fos.close();
            is.close();
        }
        catch (IOException e){
            Log.e("JSON",e.getMessage());
        }
        return ;
    }

    private URL setURL(double lat, double lng){
        StringBuilder urlBuilder =  new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json");
        urlBuilder.append("?location=" + lat + "," + lng);
        urlBuilder.append("&radius=1000");
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
            places += tmp + "|";
        }
        places = places.substring(0,places.length()-1);
        return places;
    }

}
