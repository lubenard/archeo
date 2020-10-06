package com.escatrag.dingos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

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
        String videoPathStart = "android.ressource://" + getActivity().getPackageName() + "/";
        String videoPath;

        Bundle bundle = this.getArguments();
        switch(bundle.getInt("scanned_item", 0)) {
            case 1:
                videoPath = videoPathStart + R.raw.avant_bras;
        }

    }
}
