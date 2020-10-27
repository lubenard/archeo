package com.lubenard.dingos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    private static boolean isQuittingSecondTime = false;

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("FRAGMENT_STATE", "User paused the app");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("FRAGMENT_STATE", "User resumed the app");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("FRAGMENT_STATE", "User destroyed the app");

    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        Log.d("BACKBUTTON", "I am pressing back button, count is " + count);
        if (count == 0 && isQuittingSecondTime) {
            super.onBackPressed();
            finish();
        } else if (count == 0) {
            Toast.makeText(this, getString(R.string.about_to_quit), Toast.LENGTH_SHORT).show();
            isQuittingSecondTime = true;
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