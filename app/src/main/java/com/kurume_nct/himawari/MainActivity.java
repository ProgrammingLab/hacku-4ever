package com.kurume_nct.himawari;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final int REQUEST_PERMISSION=1000;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment fragment = SupportMapFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_main, fragment);
        transaction.commit();
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        checkPermission();
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permission}, REQUEST_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{permission}, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationActivity();
            }
        }
    }

    private void locationActivity() {
        try {
            map.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Location myLocate = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                LatLng myPos = new LatLng(myLocate.getLatitude(), myLocate.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 15));
            } else {
                Log.d("LOCATE", "gps is not enabled");
            }
        } catch(SecurityException e) {
            Log.d("SECURITY", e.toString());
        }
    }
}
