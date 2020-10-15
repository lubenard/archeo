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
                questionTextView.setText("Dans quel état est l'avant bras gauche ?");
                radio1.setText("L'avant-bras gauche est intact");
                radio2.setText("L'avant-bras gauche est fracturé en son milieu");
                radio3.setText("Seul le haut de l’avant-bras gauche est fracturé");
                radio4.setText("L’avant-bras gauche est en bouillie !");
                goodAnswer = R.id.radioButton2;
                break;
            case 2:
                questionTextView.setText("Quel est le sexe du sujet ?");
                radio1.setText("Le sujet est une femme");
                radio2.setText("Le sujet est un homme");
                radio3.setText("Le sexe de ce sujet adulte n'est pas determinable");
                radio4.setText("C'est un enfant, doonc ce n'est possible sur un squelette");
                goodAnswer = R.id.radioButton1;
                break;
            case 3:
                questionTextView.setText("Dans quel état est la dentition du sujet ?");
                radio1.setText("Le sujet n'a pas de carie");
                radio2.setText("Le sujet a une carie");
                radio3.setText("Le sujet a une carie, une dent sur deux");
                radio4.setText("Le sujet n'a pas de dents");
                goodAnswer = R.id.radioButton2;
                break;
            case 4:
                questionTextView.setText("Quelle taille mesure le sujet ?");
                radio1.setText("Le sujet mesure environ 1,50m");
                radio2.setText("Le sujet mesure environ 1,65m");
                radio3.setText("Le sujet mesure 1,85m");
                radio4.setText("Il n'est pas possible d'estimer la taille");
                goodAnswer = R.id.radioButton2;
                break;
            case 5:
                questionTextView.setText("Quel âge a le sujet ?");
                radio1.setText("Le sujet est un enfant de - de 10 ans");
                radio2.setText("C'est un adolescent entre 10 et 15 ans");
                radio3.setText("C'est un adolescent entre 15 et 20 ans");
                radio4.setText("Il n'est pas possible d'estimer la taille");
                goodAnswer = R.id.radioButton4;
                break;
            case 6:
                questionTextView.setText("Quel est l'object presenté ?");
                radio1.setText("C'est un vase de l'âge du bronze");
                radio2.setText("C'est une épée de l'époque romaine");
                radio3.setText("C'est un collier en perle de l'époque mérovingigienne");
                radio4.setText("C'est un pot de fleur d'aujourd'hui");
                goodAnswer = R.id.radioButton3;
                break;
            case 7:
                questionTextView.setText("Combien y a t'il de sujets ?");
                radio1.setText("Il y a un sujet");
                radio2.setText("Il y a deux sujets");
                radio3.setText("Il y a trois sujets");
                radio4.setText("C'est le même que celui qui est sur le dos");
                goodAnswer = R.id.radioButton1;
                break;
            case 8:
                questionTextView.setText("Quel symptome peut on voir sur le tibia du sujet ?");
                radio1.setText("Le sujet présente une fracture");
                radio2.setText("Le sujet présente la syphilis");
                radio3.setText("Le sujet a un trou, une ostéomyélite (infection)");
                radio4.setText("Le tibia du sujet est très sain");
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
                        Toast.makeText(getContext(), "Well played, you did it !", Toast.LENGTH_LONG).show();
                        commitTransition();
                    }
                    else
                        Toast.makeText(getContext(), "Oops, wrong answer", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(),
                            "Please make a choice", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
