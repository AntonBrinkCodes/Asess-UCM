package com.example.asessucm.Model;


import java.io.Serializable;
import java.util.ArrayList;

public class SensorResultList implements Serializable {
    private ArrayList<SensorReading> internalSensorReading;
    private ArrayList<SensorReading> BTSensorReading;
    private boolean UCM = false;
    private double UCMAngle = 0;

    public double getUCMAngle() {
        return UCMAngle;
    }

    public void setUCMAngle(double UCMAngle) {
        this.UCMAngle = UCMAngle;
    }

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

    public boolean getUCM() {
        return UCM;
    }

    public void setUCM(boolean UCM) {
        this.UCM = UCM;
    }

    public void addToBTList(double[] newValues){
        for(double value:newValues){
            this.BTSensorReading.add(new SensorReading(value));
        }
    }

    public void addToIntList(double value){
            this.internalSensorReading.add(new SensorReading(value));

    }

    public double[] getLastFourInternal(){
        double[] readings = new double[4];
        for(int i = 0;i<4;i++){
            readings[i] = this.internalSensorReading.get(getLength()-(i+1)).angle;
        }
        return readings;
    }

    public int getLength(){
        return this.internalSensorReading.size();
    }

    public double getLastInternal() {
        if (getLength() > 0) {
            return this.internalSensorReading.get(getLength() - 1).angle;
        } else {
            return 0;
        }


    }
}
