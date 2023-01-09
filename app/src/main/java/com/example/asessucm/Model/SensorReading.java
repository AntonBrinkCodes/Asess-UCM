package com.example.asessucm.Model;

import java.io.Serializable;

public class SensorReading implements Serializable {
    double angle;
    double timestamp;

    public SensorReading(double angle, double timestamp) {
        this.angle = angle;
        this.timestamp = timestamp;
    }

    public double getAngle() {
        return this.angle;
    }
}
