package com.lubenard.dingos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.plattysoft.leonids.ParticleSystem;
import com.plattysoft.leonids.modifiers.ScaleModifier;

public class QuizzFragment extends Fragment implements View.OnClickListener {

    private int goodAnswer = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.quizz_fragment, container, false);
    }

    private void commitTransition() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        WaitScan fragment = new WaitScan();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    private String getCustomString(int resId) {
        return getContext().getString(resId);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Ding'os - QUIZZ TIME !");

        TextView questionTextView = view.findViewById(R.id.textViewQuestionQuizz);

        Button button1 = view.findViewById(R.id.answer1);
        Button button2 = view.findViewById(R.id.answer2);
        Button button3 = view.findViewById(R.id.answer3);
        Button button4 = view.findViewById(R.id.answer4);

        final int item = WaitScan.getItemChoice();

        if (item == 0 || item == 11)
            commitTransition();

        switch (item) {
            case 1:
                questionTextView.setText(getCustomString(R.string.forearm_question));
                button1.setText(getCustomString(R.string.forearm_answ1));
                button2.setText(getCustomString(R.string.forearm_answ2));
                button3.setText(getCustomString(R.string.forearm_answ3));
                button4.setText(getCustomString(R.string.forearm_answ4));
                goodAnswer = R.id.answer2;
                break;
            case 2:
                questionTextView.setText(getCustomString(R.string.sex_question));
                button1.setText(getCustomString(R.string.sex_answ1));
                button2.setText(getCustomString(R.string.sex_answ2));
                button3.setText(getCustomString(R.string.sex_answ3));
                button4.setText(getCustomString(R.string.sex_answ4));
                goodAnswer = R.id.answer1;
                break;
            case 3:
                questionTextView.setText(getCustomString(R.string.teeth_question));
                button1.setText(getCustomString(R.string.teeth_answ1));
                button2.setText(getCustomString(R.string.teeth_answ2));
                button3.setText(getCustomString(R.string.teeth_answ3));
                button4.setText(getCustomString(R.string.teeth_answ4));
                goodAnswer = R.id.answer2;
                break;
            case 4:
                questionTextView.setText(getCustomString(R.string.size_question));
                button1.setText(getCustomString(R.string.size_answ1));
                button2.setText(getCustomString(R.string.size_answ2));
                button3.setText(getCustomString(R.string.size_answ3));
                button4.setText(getCustomString(R.string.size_answ4));
                goodAnswer = R.id.answer2;
                break;
            case 5:
                questionTextView.setText(getCustomString(R.string.age_question));
                button1.setText(getCustomString(R.string.age_answ1));
                button2.setText(getCustomString(R.string.age_answ2));
                button3.setText(getCustomString(R.string.age_answ3));
                button4.setText(getCustomString(R.string.age_answ4));
                goodAnswer = R.id.answer4;
                break;
            case 6:
                questionTextView.setText(getCustomString(R.string.object_question));
                button1.setText(getCustomString(R.string.object_answ1));
                button2.setText(getCustomString(R.string.object_answ2));
                button3.setText(getCustomString(R.string.object_answ3));
                button4.setText(getCustomString(R.string.object_answ4));
                goodAnswer = R.id.answer3;
                break;
            case 7:
                questionTextView.setText(getCustomString(R.string.howmany_question));
                button1.setText(getCustomString(R.string.howmany_answ1));
                button2.setText(getCustomString(R.string.howmany_answ2));
                button3.setText(getCustomString(R.string.howmany_answ3));
                button4.setText(getCustomString(R.string.howmany_answ4));
                goodAnswer = R.id.answer1;
                break;
            case 8:
                questionTextView.setText(getCustomString(R.string.tibia_question));
                button1.setText(getCustomString(R.string.tibia_answ1));
                button2.setText(getCustomString(R.string.tibia_answ2));
                button3.setText(getCustomString(R.string.tibia_answ3));
                button4.setText(getCustomString(R.string.tibia_answ4));
                goodAnswer = R.id.answer3;
                break;
        }

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d("QUIZZ", "Good answer is " + goodAnswer + ", current answer is " + view.getId());
        if (view.getId() == goodAnswer) {
            Toast.makeText(getContext(), getContext().getString(R.string.good_answer), Toast.LENGTH_SHORT).show();
            new ParticleSystem(getActivity(), 10, R.drawable.star, 3000)
                    .setSpeedByComponentsRange(-0.2f, 0.3f, -0.2f, 0.03f)
                    .setAcceleration(0.000003f, 90)
                    .setInitialRotationRange(0, 360)
                    .setRotationSpeed(160)
                    .setFadeOut(2000)
                    .addModifier(new ScaleModifier(0f, 1.5f, 0, 1500))
                    .oneShot(view, 10);
            commitTransition();
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
