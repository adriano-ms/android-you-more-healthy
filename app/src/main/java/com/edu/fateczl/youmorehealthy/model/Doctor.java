package com.edu.fateczl.youmorehealthy.model;

import androidx.annotation.NonNull;

public class Doctor {

    private int id;
    private String name;
    private String speciality;

    public Doctor(){
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

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @NonNull
    @Override
    public String toString() {
        return getId() + ". " + getName() + " - " + getSpeciality();
    }
}
