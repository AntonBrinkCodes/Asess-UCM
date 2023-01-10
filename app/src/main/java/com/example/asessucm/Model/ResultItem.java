package com.example.asessucm.Model;

import java.io.Serializable;
import java.util.Date;

public class ResultItem implements Serializable {

    private QuestionnaireResult questionnaireResult;
    private SensorResultList sensorResultList;

    public ResultItem(QuestionnaireResult questionnaireResult, SensorResultList sensorResultList) {
        this.questionnaireResult = questionnaireResult;
        this.sensorResultList = sensorResultList;
    }

    public boolean getUCM(){
        return sensorResultList.getUCM();
    }

    public SensorResultList getSensorResultList() {
        return sensorResultList;
    }

    public QuestionnaireResult getQuestionnaireResult() {
        return questionnaireResult;
    }

    public Date getDate(){
        return questionnaireResult.getDate();
    }
}
