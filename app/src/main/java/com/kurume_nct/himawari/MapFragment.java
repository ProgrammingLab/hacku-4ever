package com.kurume_nct.himawari;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {
    private final int REQUEST_PERMISSION = 1000;
    private final int REQUEST_INPUT = 10;
    private GoogleMap map;
    private Location currentPos;
    private Marker destMarker = null;
    private Polyline line;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        checkPermission();
        map.setMyLocationEnabled(true);

        map.setOnMapLongClickListener(this);
        map.setOnMarkerClickListener(this);
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationActivity();
        } else {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermission(String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{permission}, REQUEST_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{permission}, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                switch (permission) {
                    case Manifest.permission.ACCESS_FINE_LOCATION:
                        if (grantResult == PackageManager.PERMISSION_GRANTED)
                            locationActivity();
                        break;
                    case Manifest.permission.ACCESS_COARSE_LOCATION:
                        if (grantResult == PackageManager.PERMISSION_GRANTED)
                            locationActivity();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void locationActivity() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Location myLocate = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (myLocate != null) {
                    currentPos = myLocate;
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    LatLng myPos = new LatLng(myLocate.getLatitude(), myLocate.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 15));
                }
            } else {
                Toast.makeText(getContext(), "gps位置検索をonにしてください", Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            Log.e("HOGE", e.getMessage());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentPos = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle bundle) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("DEBUG", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("DEBUG", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("DEBUG", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (destMarker != null) {
            destMarker.setPosition(latLng);
        } else {
            destMarker = map.addMarker(new MarkerOptions().position(latLng).draggable(true));
        }

        //drawRouteの確認用
        drawRoute(new ArrayList<LatLng>());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(destMarker)) {
            // markerがクリックされたときの処理
            Log.d("HOGE", "Marker Clicked");
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.activity_main, InputFormFragment.newInstance(this, REQUEST_INPUT, marker.getPosition()));
            transaction.addToBackStack(null);
            transaction.commit();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_INPUT:
                if (resultCode != Activity.RESULT_OK) break;
                Log.d("HOGE", data.getStringExtra(InputFormFragment.DURATION_KEY));
                Log.d("HOGE", data.getStringExtra(InputFormFragment.PRICE_KEY));
                break;
            default:
                break;
        }
    }

    public void drawRoute(List<LatLng> waypoints) {
        if (destMarker == null) {
            Log.e("fuga", "drawRoute");
            return;
        }

        String output = "json";

        String parameters = "";
        parameters += "origin=" + currentPos.getLatitude() + "," + currentPos.getLongitude();
        parameters += "&destination=" + destMarker.getPosition().latitude + "," + destMarker.getPosition().longitude;

        if (!waypoints.isEmpty()) parameters += "&waypoints=ptimize:true";
        for (LatLng waypoint : waypoints) {
            parameters += "|" + waypoint.latitude + "," + waypoint.longitude;
        }

        parameters += "&mode=walking";

        //ここにAPIキーを追加してください
        parameters += "&key=";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

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
                    .width(5)
                    .color(Color.RED));

            return;
        }
    }
}
