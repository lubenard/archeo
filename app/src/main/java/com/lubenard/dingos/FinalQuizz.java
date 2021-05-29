package com.lubenard.dingos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.plattysoft.leonids.ParticleSystem;
import com.plattysoft.leonids.modifiers.ScaleModifier;

public class FinalQuizz extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.final_quizz, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.final_answer1).setOnClickListener(this);
        view.findViewById(R.id.final_answer2).setOnClickListener(this);
        view.findViewById(R.id.final_answer3).setOnClickListener(this);
        view.findViewById(R.id.final_answer4).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d("FINAL_QUIZZ", "Clicked on id " + view.getId());
        if (view.getId() == R.id.final_answer2) {
            Toast.makeText(getContext(), getContext().getString(R.string.good_answer), Toast.LENGTH_SHORT).show();
            new ParticleSystem(getActivity(), 10, R.drawable.star, 3000)
                    .setSpeedByComponentsRange(-0.2f, 0.3f, -0.2f, 0.03f)
                    .setAcceleration(0.000003f, 90)
                    .setInitialRotationRange(0, 360)
                    .setRotationSpeed(160)
                    .setFadeOut(2000)
                    .addModifier(new ScaleModifier(0f, 1.5f, 0, 1500))
                    .oneShot(view, 10);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            EndFragment fragment = new EndFragment();
            fragmentTransaction.replace(android.R.id.content, fragment);
            fragmentTransaction.commit();
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.bad_answer), Toast.LENGTH_SHORT).show();
            new ParticleSystem(getActivity(), 10, R.drawable.red_cross, 3000)
                    .setSpeedByComponentsRange(-0.2f, 0.3f, -0.2f, 0.03f)
                    .setAcceleration(0.000003f, 90)
                    .setInitialRotationRange(0, 360)
                    .setRotationSpeed(160)
                    .setFadeOut(2000)
                    .addModifier(new ScaleModifier(0f, 1.5f, 0, 1500))
                    .oneShot(view, 10);
        }
    }
}
