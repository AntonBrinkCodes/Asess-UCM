package com.example.asessucm.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class representing the results of all answered questionnaire + all runs of the test.
 * sensorResultList is a nested ArrayList. each SensorResultList has the
 * sensor data from that run in two ArrayLists.
 */
public class ResultList implements Serializable {

    private ArrayList<QuestionnaireResult> resultList;
    private ArrayList<SensorResultList> sensorResultLists;

    public ResultList(ArrayList<QuestionnaireResult> resultList) {
        this.resultList = resultList;
    }

    public ResultList(){
        this.resultList = new ArrayList<>();
    }

    public ArrayList<QuestionnaireResult> getResultList() {
        return resultList;
    }

    public void setResultList(ArrayList<QuestionnaireResult> resultList) {
        this.resultList = resultList;
    }
}
