package com.lubenard.dingos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import static android.view.View.GONE;
import static android.view.View.resolveSizeAndState;

public class MainActivity extends AppCompatActivity {

    private static boolean isQuittingSecondTime = false;

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("FRAGMENT_STATE", "User paused the app");
        WaitScan.setIsConnectionAlive(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("FRAGMENT_STATE", "User resumed the app");
        WaitScan.setIsConnectionAlive(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("FRAGMENT_STATE", "User destroyed the app");
        WaitScan.interruptThread();
        WaitScan.getBluetoothDataReceiver().closeConnection();
    }

    /*@Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        Log.d("BACKBUTTON", "I am pressing back button, count is " + count);
        if (!VideoPlayerFragment.getIsInsideVideo()) {

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
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_activity);
        findViewById(android.R.id.content).getRootView().setBackgroundColor(getResources().getColor(R.color.splashscreenColor));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.splashscreenImageView).setVisibility(GONE);
                // change background to white
                findViewById(android.R.id.content).getRootView().setBackgroundColor(getResources().getColor(android.R.color.white));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                LaunchingFragment fragment = new LaunchingFragment();
                fragmentTransaction.replace(android.R.id.content, fragment);
                fragmentTransaction.commit();
            }
        }, 2000);
    }
}