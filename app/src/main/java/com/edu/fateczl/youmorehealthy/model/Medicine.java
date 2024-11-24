package com.edu.fateczl.youmorehealthy.model;

import androidx.annotation.NonNull;

public class Medicine {

    private int id;
    private String name;
    private String dose;

    public Medicine(){
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    @NonNull
    @Override
    public String toString() {
        return getId() + ". " + getName() + " | " + getDose();
    }
}
