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

public class QuizzFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.quizz_fragment, container, false);
    }

    private void commitTransition() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("launchThread", false);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        WaitScan fragment = new WaitScan();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private String getCustomString(int resId) {
        return getContext().getString(resId);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView questionTextView = view.findViewById(R.id.textViewQuestionQuizz);

        final RadioGroup radioGroup = view.findViewById(R.id.quizzRadioGroup);
        RadioButton radio1 = view.findViewById(R.id.radioButton1);
        RadioButton radio2 = view.findViewById(R.id.radioButton2);
        RadioButton radio3 = view.findViewById(R.id.radioButton3);
        RadioButton radio4 = view.findViewById(R.id.radioButton4);

        Log.d("QUIZZ", "RadioGroup is " + radioGroup);

        final int item = WaitScan.getItemChoice();

        if (item == 0 || item == 11)
            commitTransition();

        int goodAnswer = 0;

        switch (item) {
            case 1:
                questionTextView.setText(getCustomString(R.string.forearm_question));
                radio1.setText(getCustomString(R.string.forearm_answ1));
                radio2.setText(getCustomString(R.string.forearm_answ2));
                radio3.setText(getCustomString(R.string.forearm_answ3));
                radio4.setText(getCustomString(R.string.forearm_answ4));
                goodAnswer = R.id.radioButton2;
                break;
            case 2:
                questionTextView.setText(getCustomString(R.string.sex_question));
                radio1.setText(getCustomString(R.string.sex_answ1));
                radio2.setText(getCustomString(R.string.sex_answ2));
                radio3.setText(getCustomString(R.string.sex_answ3));
                radio4.setText(getCustomString(R.string.sex_answ4));
                goodAnswer = R.id.radioButton1;
                break;
            case 3:
                questionTextView.setText(getCustomString(R.string.teeth_question));
                radio1.setText(getCustomString(R.string.teeth_answ1));
                radio2.setText(getCustomString(R.string.teeth_answ2));
                radio3.setText(getCustomString(R.string.teeth_answ3));
                radio4.setText(getCustomString(R.string.teeth_answ4));
                goodAnswer = R.id.radioButton2;
                break;
            case 4:
                questionTextView.setText(getCustomString(R.string.size_question));
                radio1.setText(getCustomString(R.string.size_answ1));
                radio2.setText(getCustomString(R.string.size_answ2));
                radio3.setText(getCustomString(R.string.size_answ3));
                radio4.setText(getCustomString(R.string.size_answ4));
                goodAnswer = R.id.radioButton2;
                break;
            case 5:
                questionTextView.setText(getCustomString(R.string.age_question));
                radio1.setText(getCustomString(R.string.age_answ1));
                radio2.setText(getCustomString(R.string.age_answ2));
                radio3.setText(getCustomString(R.string.age_answ3));
                radio4.setText(getCustomString(R.string.age_answ4));
                goodAnswer = R.id.radioButton4;
                break;
            case 6:
                questionTextView.setText(getCustomString(R.string.object_question));
                radio1.setText(getCustomString(R.string.object_answ1));
                radio2.setText(getCustomString(R.string.object_answ2));
                radio3.setText(getCustomString(R.string.object_answ3));
                radio4.setText(getCustomString(R.string.object_answ4));
                goodAnswer = R.id.radioButton3;
                break;
            case 7:
                questionTextView.setText(getCustomString(R.string.howmany_question));
                radio1.setText(getCustomString(R.string.howmany_answ1));
                radio2.setText(getCustomString(R.string.howmany_answ2));
                radio3.setText(getCustomString(R.string.howmany_answ3));
                radio4.setText(getCustomString(R.string.howmany_answ4));
                goodAnswer = R.id.radioButton1;
                break;
            case 8:
                questionTextView.setText(getCustomString(R.string.tibia_question));
                radio1.setText(getCustomString(R.string.tibia_answ1));
                radio2.setText(getCustomString(R.string.tibia_answ2));
                radio3.setText(getCustomString(R.string.tibia_answ3));
                radio4.setText(getCustomString(R.string.tibia_answ4));
                goodAnswer = R.id.radioButton3;
                break;
        }

        Button checkAnswer = view.findViewById(R.id.verifyAnswer);

        final int finalGoodAnswer = goodAnswer;
        checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("QUIZZ", "RadioGroup is " + radioGroup);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    Log.d("QUIZZ", "Selection made by user is number " + selectedId);
                    Log.d("QUIZZ", "goodAnswer is " + finalGoodAnswer);
                    if (selectedId == finalGoodAnswer) {
                        Toast.makeText(getContext(), getContext().getString(R.string.good_answer), Toast.LENGTH_LONG).show();
                        new ParticleSystem(getActivity(), 10, R.drawable.star, 3000)
                                .setSpeedByComponentsRange(-0.2f, 0.3f, -0.2f, 0.03f)
                                .setAcceleration(0.000003f, 90)
                                .setInitialRotationRange(0, 360)
                                .setRotationSpeed(160)
                                .setFadeOut(2000)
                                .addModifier(new ScaleModifier(0f, 1.5f, 0, 1500))
                                .oneShot(view, 10);
                        commitTransition();
                    }
                    else
                        Toast.makeText(getContext(), getContext().getString(R.string.bad_answer), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(),
                            getContext().getString(R.string.no_answer), Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
