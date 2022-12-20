package com.example.asessucm.Model;

import java.io.Serializable;

public class SensorReading implements Serializable {
    float angle;
    float timestamp;

    public SensorReading(float angle, float timestamp) {
        this.angle = angle;
        this.timestamp = timestamp;
    }
}
