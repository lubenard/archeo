package com.escatrag.dingos;

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

public class QuizzFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.quizz_fragment, container, false);
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

        final int item = 1;
        RadioButton goodAnswer = null;

        switch (item) {
            case 1:
                questionTextView.setText("Dans quel état est l'avant bras gauche ?");
                radio1.setText("L'avant-bras gauche est intact");
                radio2.setText("L'avant-bras gauche est fracturé en son milieu");
                radio3.setText("Seul le haut de l’avant-bras gauche est fracturé");
                radio4.setText("L’avant-bras gauche est en bouillie !");
                goodAnswer = radio2;
                break;
        }

        Button checkAnswer = view.findViewById(R.id.verifyAnswer);

        checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("QUIZZ", "RadioGroup is " + radioGroup);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    Log.d("QUIZZ", "Selection made by user is number " + selectedId);
                    RadioButton radioButtonAnswer = getView().findViewById(selectedId);
                    Log.d("QUIZZ", "Text of selections is " + radioButtonAnswer.getText());
                } else {
                    Toast.makeText(getContext(),
                            "Please make a choice", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
