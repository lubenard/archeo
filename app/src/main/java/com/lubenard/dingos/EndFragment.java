package com.lubenard.dingos;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.plattysoft.leonids.ParticleSystem;

public class EndFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.end_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button exitApp = view.findViewById(R.id.exitApp);

        exitApp.setOnClickListener(view1 -> {
            // Remove saved user progress before exiting
            SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            preferences.edit().remove("DISCOVERED_PROGRESS").apply();
            WaitScan.resetDiscoveryArray();
            // Clear back stack to avoid using back button
            FragmentManager fm = getActivity().getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            LaunchingFragment fragment = new LaunchingFragment();
            fragmentTransaction.replace(android.R.id.content, fragment);
            fragmentTransaction.commit();
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height_max_gen = displayMetrics.heightPixels - 200;
        int width_max_gen = displayMetrics.widthPixels - 200;

        for (int i = 0; i < 10; i++) {
            ParticleSystem ps = new ParticleSystem(getActivity(), 100, R.drawable.star_red, 800);
            ps.setScaleRange(0.7f, 1.3f);
            ps.setSpeedRange(0.1f, 0.25f);
            ps.setRotationSpeedRange(90, 180);
            ps.setFadeOut(200, new AccelerateInterpolator());
            ps.emit((int) (Math.random() * (width_max_gen - 200 + 1) + 200), (int) (Math.random() * (height_max_gen - 200 + 1) + 200), 70, 500);

            ParticleSystem ps2 = new ParticleSystem(getActivity(), 100, R.drawable.star_blue, 800);
            ps2.setScaleRange(0.7f, 1.3f);
            ps2.setSpeedRange(0.1f, 0.25f);
            ps2.setRotationSpeedRange(90, 180);
            ps2.setFadeOut(200, new AccelerateInterpolator());
            ps2.emit((int) (Math.random() * (width_max_gen - 200 + 1) + 200), (int) (Math.random() * (height_max_gen - 200 + 1) + 200), 70, 500);
        }
    }
}
