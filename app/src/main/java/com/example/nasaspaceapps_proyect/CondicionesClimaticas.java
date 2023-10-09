package com.example.nasaspaceapps_proyect;

public class CondicionesClimaticas {
    double speed;
    double temperature;

    public CondicionesClimaticas(double speed, double temperature) {
        this.speed = speed;
        this.temperature = temperature;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}

