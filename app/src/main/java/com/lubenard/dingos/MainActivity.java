package com.lubenard.dingos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        Log.d("BACKBUTTON", "I am pressing back button, count is " + count);
        if (count == 0) {
            super.onBackPressed();
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_activity);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.splashscreenImageView).setVisibility(GONE);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                LaunchingFragment fragment = new LaunchingFragment();
                fragmentTransaction.replace(android.R.id.content, fragment);
                fragmentTransaction.commit();
            }
        }, 2000);
    }
}