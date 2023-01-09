package com.example.asessucm;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.asessucm.uiutils.MsgUtils;
import com.example.asessucm.utils.FileHandler;


public class QuestionnaireActivity extends FragmentActivity {

    private Handler handler;
    private Button saveBtn;
    private QuestionnaireFragment questionnaireFragment;
    private GraphFragment graphFragment;

    private final static String LOG_TAG = "QuestionnaireActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionnaire_layout);
        handler = new Handler();
        saveBtn = findViewById(R.id.questionnaire_save_button);
        saveBtn.setOnClickListener(this::saveBtn);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        questionnaireFragment = new QuestionnaireFragment();
        graphFragment = new GraphFragment();
        ft.replace(R.id.questions_fragment_container, graphFragment);
        ft.commit();

    }

    private void saveBtn(View view) {
        Log.i(LOG_TAG, "Questionnaire score is: " + questionnaireFragment.getResult());
        handler.post(() -> MsgUtils.createDialog("Questionnaire Score:", Integer.toString(questionnaireFragment.getResult()), QuestionnaireActivity.this).show());
    }

}
