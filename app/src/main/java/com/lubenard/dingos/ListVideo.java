package com.lubenard.dingos;

import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class ListVideo extends Fragment {

    private static FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.list_videos_fragment, container, false);
    }

    private void commitTransition() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentManager = getFragmentManager();

        /* This code is messy but fix the bug of superposition of fragment when using .addToBackStack(null) */
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event ) {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                   Log.d("LISTVIDEO", "BACK HAS BEEN PRESSED");
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    WaitScan fragment = new WaitScan();
                    fragmentTransaction.replace(android.R.id.content, fragment);
                    fragmentTransaction.commit();
                   return true;
                }
                return false;
            }
        });

        ArrayList<Button> buttons = new ArrayList<>();

        buttons.add((Button)view.findViewById(R.id.replay_intro));
        buttons.add((Button)view.findViewById(R.id.replay_avant_bras));
        buttons.add((Button)view.findViewById(R.id.replay_coxaux));
        buttons.add((Button)view.findViewById(R.id.replay_crane));
        buttons.add((Button)view.findViewById(R.id.replay_femur));
        buttons.add((Button)view.findViewById(R.id.replay_humerus));
        buttons.add((Button)view.findViewById(R.id.replay_object));
        buttons.add((Button)view.findViewById(R.id.replay_reduction));
        buttons.add((Button)view.findViewById(R.id.replay_tibia));

        ArrayList<Integer> discoveredArray = WaitScan.getElementDiscoveredArray();
        final int[] resArray = WaitScan.getResArray();

        for (int i = 0; i < buttons.size(); i++) {
            if (!discoveredArray.contains(i)) {
                buttons.get(i).setEnabled(false);
            } else {
                final int finalI = i;
                buttons.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Launch correct video
                        WaitScan.setShouldQuizzLaunch(2);
                        WaitScan.setItemChoice(finalI, resArray[finalI]);
                        commitTransition();
                    }
                });
            }
        }
    }
}
