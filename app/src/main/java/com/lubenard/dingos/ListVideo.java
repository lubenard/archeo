package com.lubenard.dingos;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class ListVideo extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.list_videos_fragment, container, false);
    }

    private void commitTransition() {
        // Disable isConnectionAlive to avoid being able to scan during quizz or video
        //WaitScan.setIsConnectionAlive(false);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        buttons.add((Button)view.findViewById(R.id.replay_photo));

        ArrayList<Integer> discoveredArray = WaitScan.getElementDiscoveredArray();
        final int[] resArray = WaitScan.getResArray();

        for (int i = 0; i < buttons.size(); i++)
        {
            if (!discoveredArray.contains(i)) {
                buttons.get(i).setEnabled(false);
            } else {
                final int finalI = i;
                buttons.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Launch correct video
                        WaitScan.setShouldQuizzLaunch(false);
                        WaitScan.setItemChoice(finalI, resArray[finalI]);
                        commitTransition();
                    }
                });
            }
        }
    }
}
