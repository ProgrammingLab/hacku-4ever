package com.kurume_nct.himawari;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {
    private final int REQUEST_PERMISSION = 1000;
    private final int REQUEST_INPUT = 10;
    private GoogleMap map;
    private Location currentPos;
    private Marker destMarker = null;

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
                Bundle args = data.getExtras();
                int is_submit = args.getInt(InputFormFragment.IS_SUBMIT);
                if (is_submit == 1) {
                    int price = args.getInt(InputFormFragment.PRICE_KEY);
                    int hour = args.getInt(InputFormFragment.DURATION_HOUR);
                    int minute = args.getInt(InputFormFragment.DURATION_MINUTE);
                    Log.d("HOGE", "price " + String.valueOf(price));
                    Log.d("HOGE", "hour " + String.valueOf(hour));
                    Log.d("HOGE", "minute " + String.valueOf(minute));
                }
                break;
            default:
                break;
        }
    }
}
