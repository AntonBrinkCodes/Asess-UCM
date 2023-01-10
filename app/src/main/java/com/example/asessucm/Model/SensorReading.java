package com.example.asessucm.Model;

import java.io.Serializable;

public class SensorReading implements Serializable {
    double angle;

    public SensorReading(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return this.angle;

    }
}
