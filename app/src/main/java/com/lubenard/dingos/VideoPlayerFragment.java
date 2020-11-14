package com.lubenard.dingos;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
    private static VideoView videoView;
    private static boolean currentVideoPlayerStatus = true;
    private static boolean isInsideVideo = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.video_player_fragment, container, false);
    }

    public static void setVideoPlayerStatus() {
        // currentVideoPlayerStatus:
        // TRUE : PLAYING
        // FALSE : PAUSED
        if (!currentVideoPlayerStatus) {
            videoView.start();
            currentVideoPlayerStatus = true;
        } else {
            videoView.pause();
            currentVideoPlayerStatus = false;
        }
    }

    public static boolean getIsInsideVideo() {
        return isInsideVideo;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        videoView = view.findViewById(R.id.videoViewPlayer);
        String videoPath;

        videoPath = "android.resource://" + getContext().getPackageName() +"/" + WaitScan.getVideoPathChoice();

        Log.d("VIDEOVIEW", "Video path is " + videoPath);

        videoView.setVideoURI(Uri.parse(videoPath));

        MediaController mediaController = new MediaController(getContext());
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) videoView.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        videoView.setLayoutParams(params);

        isInsideVideo = true;

        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                WaitScan.setIsConnectionAlive(false);
                isInsideVideo = false;
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment;
                if (WaitScan.getShouldQuizzLaunch()) {
                    Log.d("VIDEOVIEW", "Video is finished now, let's go to the quizz");
                    fragment = new QuizzFragment();
                } else if (!WaitScan.getShouldQuizzLaunch()) {
                    Log.d("VIDEOVIEW", "Video is finished now, let's go to the quizz");
                    fragment = new WaitScan();
                } else if (WaitScan.getVideoPathChoice() == R.raw.photo) {
                    Log.d("VIDEOVIEW", "Video is finished now, let's go to the End Fragment");
                    fragment = new EndFragment();
                } else {
                    Log.d("VIDEOVIEW", "Since we should not go to quizz, let's go to replay");
                    fragment = new ListVideo();
                }
                fragmentTransaction.replace(android.R.id.content, fragment);
                fragmentTransaction.commit();
            }
        });
    }
}
