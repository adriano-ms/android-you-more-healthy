package com.edu.fateczl.youmorehealthy.model;

import androidx.annotation.NonNull;

public class Frequency {

    private int hour;
    private int minute;
    private int cycleInHours;
    private int repetitions;

    public Frequency(){
        super();
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getCycleInHours() {
        return cycleInHours;
    }

    public void setCycleInHours(int cycleInHours) {
        this.cycleInHours = cycleInHours;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    @NonNull
    @Override
    public String toString() {
        return getHour() + ":" + getMinute() + " C: " + getCycleInHours() + "h R:" + getRepetitions() + " times";
    }
}
