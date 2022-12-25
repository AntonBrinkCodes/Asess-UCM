package com.example.asessucm;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.asessucm.Model.QuestionnaireResult;

public class QuestionnaireFragment extends Fragment {
    //TODO: CHANGE FROM CARDVIEWS TO SEEKBARS AND HOOK THE VIEWS AND THE SAVE BUTTON AND ETC.
    private CardView[] questionCards = new CardView[13];
    private QuestionnaireResult result;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_questionnaire, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        Resources r = view.getResources();
        String name = view.getContext().getPackageName();
        int[] cardIds = new int[13];
        for(int i = 0; i<3;i++){
            cardIds[i] = r.getIdentifier("question_" +i+1, "id", name);
            questionCards[i] = view.findViewById(cardIds[i]);

        }
    }
}