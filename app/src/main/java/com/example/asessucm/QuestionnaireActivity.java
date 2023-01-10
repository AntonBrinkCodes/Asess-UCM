package com.example.asessucm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.asessucm.Model.QuestionnaireResult;
import com.example.asessucm.Model.ResultItem;
import com.example.asessucm.Model.SensorResultList;
import com.example.asessucm.uiutils.MsgUtils;
import com.example.asessucm.utils.FileHandler;

import java.io.File;
import java.util.ArrayList;


public class QuestionnaireActivity extends FragmentActivity {

    private Handler handler;
    private Button saveBtn;
    private QuestionnaireFragment questionnaireFragment;
    private FileHandler fileHandler;

    ArrayList<ResultItem> results;
    SensorResultList sensorResult;


    private final static String LOG_TAG = "QuestionnaireActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionnaire_layout);

        fileHandler = new FileHandler();

        if(fileHandler.loadResults(this)==null){
            results = new ArrayList<ResultItem>();

        } else{
            results = (ArrayList<ResultItem>) fileHandler.loadResults(this);
        }

        sensorResult = fileHandler.loadAnglesResults(this);


        handler = new Handler();
        saveBtn = findViewById(R.id.questionnaire_save_button);
        saveBtn.setOnClickListener(this::saveBtn);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        questionnaireFragment = new QuestionnaireFragment();
        ft.replace(R.id.questions_fragment_container, questionnaireFragment);
        ft.commit();

    }

    /**
     * Calculates average score according to SPADI.
     * All answers are assumed to be filled.
     *
     * @param view
     */
    private void saveBtn(View view) {
       // Log.i(LOG_TAG, "Questionnaire score is: " + questionnaireFragment.getResult());
        float questionnaireScore = questionnaireFragment.getResult();
        QuestionnaireResult questionnaireResult = new QuestionnaireResult(questionnaireScore);
        if(results.size()>0){
            float scoreDelta = questionnaireScore - results.get(results.size()-1).getQuestionnaireResult().getScore();
            questionnaireResult.setScoreDelta(scoreDelta);
        }

        results.add(new ResultItem(questionnaireResult, sensorResult));
        FileHandler.saveResults(results, this);
        Log.i("nr of results saved","Size is: "+results.size());
        Intent intent = new Intent(QuestionnaireActivity.this, ResultActivity.class);
        startActivity(intent);
        finish();

    }

}
