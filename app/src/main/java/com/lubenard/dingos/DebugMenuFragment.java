package com.lubenard.dingos;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DebugMenuFragment extends Fragment implements View.OnClickListener {
    private static FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.debug_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WaitScan.setDebugMode(1);

        fragmentManager = getFragmentManager();

        /* This code is messy but fix the bug of superposition of fragment when using .addToBackStack(null) */
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event ) {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.d("DEBUG_MENU", "BACK HAS BEEN PRESSED");
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    AboutFragment fragment = new AboutFragment();
                    fragmentTransaction.replace(android.R.id.content, fragment);
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });

        Button intro = view.findViewById(R.id.debug_launch_intro);
        Button video1 = view.findViewById(R.id.debug_launch_video1);
        Button video2 = view.findViewById(R.id.debug_launch_video2);
        Button video3 = view.findViewById(R.id.debug_launch_video3);
        Button video4 = view.findViewById(R.id.debug_launch_video4);
        Button video5 = view.findViewById(R.id.debug_launch_video5);
        Button video6 = view.findViewById(R.id.debug_launch_video6);
        Button video7 = view.findViewById(R.id.debug_launch_video7);
        Button video8 = view.findViewById(R.id.debug_launch_video8);
        Button video9 = view.findViewById(R.id.debug_launch_video9);

        intro.setOnClickListener(this);
        video1.setOnClickListener(this);
        video2.setOnClickListener(this);
        video3.setOnClickListener(this);
        video4.setOnClickListener(this);
        video5.setOnClickListener(this);
        video6.setOnClickListener(this);
        video7.setOnClickListener(this);
        video8.setOnClickListener(this);
        video9.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int itemIndex = -1;
        switch (view.getId()) {
            case R.id.debug_launch_intro:
                itemIndex = 0;
                break;
            case R.id.debug_launch_video1:
                itemIndex = 1;
                break;
            case R.id.debug_launch_video2:
                itemIndex = 2;
                break;
            case R.id.debug_launch_video3:
                itemIndex = 3;
                break;
            case R.id.debug_launch_video4:
                itemIndex = 4;
                break;
            case R.id.debug_launch_video5:
                itemIndex = 5;
                break;
            case R.id.debug_launch_video6:
                itemIndex = 6;
                break;
            case R.id.debug_launch_video7:
                itemIndex = 7;
                break;
            case R.id.debug_launch_video8:
                itemIndex = 8;
                break;
            case R.id.debug_launch_video9:
                itemIndex = 9;
                break;
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment;
        if (itemIndex != 9) {
            WaitScan.setShouldQuizzLaunch(1);
            WaitScan.setItemChoice(itemIndex, WaitScan.getResArray()[itemIndex]);
            fragment = new VideoPlayerFragment();
        } else
            fragment = new FinalQuizz();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }
}
