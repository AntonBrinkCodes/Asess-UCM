package com.example.asessucm.Sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class InternalGyrometerSensor implements SensorEventListener {
    private GyrometerListener listener;
    private SensorManager sensorManager;
    private Sensor sensor;
    private final String tag = "SENSORMAN";
    private int samplingRate = 19231;

    public InternalGyrometerSensor(SensorManager sensorManager){
        this.sensorManager = sensorManager;
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(listener != null){
            listener.onGyroChanged(sensorEvent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void start(){
        this.sensorManager.registerListener(this, sensor, samplingRate);
        Log.i(tag, "GYRO SENSOR STARTED");
    }

    public void pause(){this.sensorManager.unregisterListener(this, sensor);
        Log.i(tag, "Gyro sensor PAUSED");}

    public void setListener(GyrometerListener listener) {
        this.listener = listener;
    }

    public interface GyrometerListener {
        void onGyroChanged(SensorEvent sensorEvent);
    }
}
