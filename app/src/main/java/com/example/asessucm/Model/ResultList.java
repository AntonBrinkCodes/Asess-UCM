package com.example.asessucm.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class representing the results of all answered questionnaire + all runs of the test.
 * sensorResultList is a nested ArrayList. each SensorResultList has the
 * sensor data from that run in two ArrayLists.
 */
public class ResultList implements Serializable {

    private ArrayList<ResultItem> results;
    public ResultList(){
        this.results = new ArrayList<>();
    }
    public ResultList(ArrayList<ResultItem> results) {
        this.results = results;
    }

    public void add(ResultItem result){
        results.add(result);
    }

    public ArrayList<ResultItem> getResults() {
        return results;
    }

    public void setResults(ArrayList<ResultItem> results) {
        this.results = results;
    }
}
