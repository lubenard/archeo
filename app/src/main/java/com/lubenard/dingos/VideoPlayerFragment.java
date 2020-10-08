package com.lubenard.dingos;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class VideoPlayerFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.video_player, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        VideoView videoView = view.findViewById(R.id.videoViewPlayer);
        String videoPath;

        videoPath = "android.resource://com.escatrag.dingos/" + WaitScan.getVideoPathChoice();

        Log.d("VIDEOVIEW", "Video path is " + videoPath);
        Log.d("VIDEOVIEW", "Video path is " + videoPath);

        videoView.setVideoURI(Uri.parse(videoPath));

        MediaController mediaController = new MediaController(getContext());
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("VIDEOVIEW", "Video is finished now, let's go to the quizz");
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                QuizzFragment fragment = new QuizzFragment();
                fragmentTransaction.replace(android.R.id.content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
}
