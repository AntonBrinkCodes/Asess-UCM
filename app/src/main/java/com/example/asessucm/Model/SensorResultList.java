package com.example.asessucm.Model;


import java.io.Serializable;
import java.util.ArrayList;

public class SensorResultList implements Serializable {
    private ArrayList<SensorReading> internalSensorReading;
    private ArrayList<SensorReading> BTSensorReading;

    public SensorResultList(ArrayList<SensorReading> internalSensorReading, ArrayList<SensorReading> BTSensorReading) {
        this.internalSensorReading = internalSensorReading;
        this.BTSensorReading = BTSensorReading;
    }

    public SensorResultList() {
        this.internalSensorReading = new ArrayList<>();
        this.BTSensorReading = new ArrayList<>();
    }

    public ArrayList<SensorReading> getInternalSensorReading() {
        return internalSensorReading;
    }

    public void setInternalSensorReading(ArrayList<SensorReading> internalSensorReading) {
        this.internalSensorReading = internalSensorReading;
    }

    public ArrayList<SensorReading> getBTSensorReading() {
        return BTSensorReading;
    }

    public void setBTSensorReading(ArrayList<SensorReading> BTSensorReading) {
        this.BTSensorReading = BTSensorReading;
    }
}
