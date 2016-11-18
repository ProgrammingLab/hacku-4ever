package com.kurume_nct.himawari;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SplashFragment splashFragment = SplashFragment.newInstance();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_main,splashFragment);
        transaction.commit();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                transaction.remove(splashFragment);

                MapFragment fragment = MapFragment.newInstance();

                transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.activity_main, fragment);
                transaction.commit();
                fragment.getMapAsync(fragment);
            }
        },1000);

        Log.d("test","HOGE");


    }
}
