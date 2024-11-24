package com.edu.fateczl.youmorehealthy.model;

import androidx.annotation.NonNull;

public enum TaskType {

    EVENT("EVENT"),
    ROUTINE("ROUTINE"),
    SPORT("SPORT"),
    MEDICINE("MEDICINE"),
    MEDIC("MEDIC");

    private final String description;

    TaskType(String description) {
        this.description = description;
    }

    private String getDescription(){
        return description;
    }

    @NonNull
    @Override
    public String toString() {
        return getDescription();
    }
}
