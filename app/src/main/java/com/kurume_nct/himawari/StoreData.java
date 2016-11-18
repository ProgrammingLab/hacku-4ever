package com.kurume_nct.himawari;


import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class StoreData {
    private LatLng latLng;
    private String name;
    private int price;
    private int stayedTime;
    private List<String> types;

    public StoreData(){

    }

    public StoreData(LatLng latLng, String name, List<String> types){
        this.latLng = latLng;
        this.name = name;
        this.types = types;
        this.price = classifyStore();
        this.stayedTime = this.classifyTime();
    }

    public StoreData(LatLng latLng, String name, int price, List<String> types){
        this.latLng = latLng;
        this.name = name;
        this.price = price;
        this.types = types;
        this.stayedTime = this.classifyTime();
    }

    public StoreData(LatLng latLng, String name, int price, List<String> types, int stayedTime){
    this.latLng = latLng;
        this.name = name;
        this.price = price;
        this.types = types;
        this.stayedTime = stayedTime;
    }

    /*getter*/
    public LatLng getLatLng(){return this.latLng;}
    public String getStoreName(){return this.name;}
    public int getPrice(){
        this.price = classifyStore();
        return this.price;
    }
    public List<String> getTypes(){return this.types;}
    public int getStayedTime(){return this.stayedTime;}

    /*setter*/
    public void setLatLng(LatLng latLng){this.latLng = latLng;}
    public void setStoreName(String name){this.name = name;}
    public void setPrice(int price){this.price = price;}
    public void setTypes(List<String> types){
        this.types = types;
        this.price = classifyStore();
        this.stayedTime = classifyTime();
    }
    public void setStayedTime(int stayedTime){this.stayedTime = stayedTime;}

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

    private int classifyTime(){
        int maxValue = 0;
        for (String tmp : this.types) {
            int s = 0;
            switch (tmp){
                case "amusement_park":
                    s = 120;
                    break;
                case "aquarium":
                    s = 60;
                    break;
                case "art_gallery":
                    s = 60;
                    break;
                case "book_store":
                    s = 15;
                    break;
                case "bowling_alley":
                    s = 60;
                    break;
                case "cafe":
                    s = 30;
                    break;
                case "church":
                    s = 10;
                    break;
                case "department_store":
                    s = 30;
                    break;
                case  "library":
                    s = 30;
                    break;
                case "movie_theater":
                    s = 120;
                    break;
                case "museum":
                    s = 100;
                    break;
                case "park":
                    s = 30;
                    break;
                case "restaurant":
                    s = 45;
                    break;
                case "spa":
                    s = 45;
                    break;
                case  "shopping_mall":
                    s = 30;
                    break;
                case "zoo":
                    s = 90;
                    break;
            }
            maxValue = Math.max(maxValue,s);
        }
        return maxValue*60;
    }

}
