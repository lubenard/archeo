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

        view.findViewById(R.id.imageButton).setOnClickListener(this);
        view.findViewById(R.id.imageButton2).setOnClickListener(this);
        view.findViewById(R.id.imageButton3).setOnClickListener(this);
        view.findViewById(R.id.imageButton4).setOnClickListener(this);
        view.findViewById(R.id.imageButton5).setOnClickListener(this);
        view.findViewById(R.id.imageButton6).setOnClickListener(this);
        view.findViewById(R.id.imageButton7).setOnClickListener(this);
        view.findViewById(R.id.imageButton8).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d("FINAL_QUIZZ", "Clicked on id " + view.getId());
        if (view.getId() == R.id.imageButton3) {
            Toast.makeText(getContext(), getContext().getString(R.string.good_answer), Toast.LENGTH_SHORT).show();
            new ParticleSystem(getActivity(), 10, R.drawable.star, 3000)
                    .setSpeedByComponentsRange(-0.2f, 0.3f, -0.2f, 0.03f)
                    .setAcceleration(0.000003f, 90)
                    .setInitialRotationRange(0, 360)
                    .setRotationSpeed(160)
                    .setFadeOut(2000)
                    .addModifier(new ScaleModifier(0f, 1.5f, 0, 1500))
                    .oneShot(view, 10);
            Bundle bundle = new Bundle();
            bundle.putBoolean("launchThread", false);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            WaitScan fragment = new WaitScan();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(android.R.id.content, fragment);
            fragmentTransaction.commit();
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.bad_answer), Toast.LENGTH_SHORT).show();
        }
    }
}
