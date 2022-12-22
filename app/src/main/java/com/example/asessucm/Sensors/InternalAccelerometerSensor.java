package com.example.asessucm.Sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class InternalAccelerometerSensor implements SensorEventListener {
    private AccelerometerListener listener;
    private SensorManager sensorManager;
    private Sensor sensor;
    private final String tag = "SENSORMAN";
    private int samplingRate = 19231;

    public InternalAccelerometerSensor(SensorManager sensorManager){
        this.sensorManager = sensorManager;
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public InternalAccelerometerSensor(SensorManager sensorManager, int samplingRate){
        this.sensorManager = sensorManager;
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.samplingRate = samplingRate;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(listener != null){
            listener.onAccSensorChanged(sensorEvent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void start(){
        this.sensorManager.registerListener(this, sensor, samplingRate);
    }

    public void pause(){this.sensorManager.unregisterListener(this, sensor);
        Log.i(tag, "Acc sensor PAUSED");}

    public interface AccelerometerListener {
        void onAccSensorChanged(SensorEvent sensorEvent);
    }

    public void setListener(AccelerometerListener listener){
        this.listener = listener;
    }
}

