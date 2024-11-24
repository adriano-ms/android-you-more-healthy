package com.edu.fateczl.youmorehealthy.model;

import androidx.annotation.NonNull;

public class Routine extends Task {

    private Frequency frequency;

    public Routine() {
        setType(TaskType.ROUTINE);
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " | " + getFrequency();
    }
}
