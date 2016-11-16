package com.kurume_nct.himawari;


import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class StoreData {
    private LatLng latLng;
    private String name;
    private int price;
    private List<String> types;

    public StoreData(){

    }

    public StoreData(LatLng latLng, String name, List<String> types){
        this.latLng = latLng;
        this.name = name;
        this.types = types;
        this.price = classifyStore();
    }

    public StoreData(LatLng latLng, String name, int price, List<String> types){
        this.latLng = latLng;
        this.name = name;
        this.price = price;
        this.types = types;
    }

    /*getter*/
    public LatLng getLatLng(){return this.latLng;}
    public String getStoreName(){return this.name;}
    public int getPrice(){
        this.price = classifyStore();
        return this.price;
    }
    public List<String> getTypes(){return this.types;}

    /*setter*/
    public void setLatLng(LatLng latLng){this.latLng = latLng;}
    public void setStoreName(String name){this.name = name;}
    public void setPrice(int price){this.price = price;}
    public void setTypes(List<String> types){
        this.types = types;
        this.price = classifyStore();
    }

    private int classifyStore(){
        int maxValue = 0;
        for (String tmp : this.types) {
            int s = 0;
            switch (tmp){
                case "amusement_park":
                    s = 3000;
                    break;
                case "aquarium":
                    s = 2000;
                    break;
                case "art_gallery":
                    s = 1000;
                    break;
                case "book_store":
                    s = 1000;
                    break;
                case "bowling_alley":
                    s = 1500;
                    break;
                case "cafe":
                    s = 1000;
                    break;
                case "department_store":
                    s = 2000;
                    break;
                case "movie_theater":
                    s = 2000;
                    break;
                case "museum":
                    s = 500;
                    break;
                case "restaurant":
                    s = 1000;
                    break;
                case "spa":
                    s = 500;
                    break;
                case "zoo":
                    s = 1000;
                    break;
            }
            maxValue = Math.max(maxValue,s);
        }
        return maxValue;
    }

}
