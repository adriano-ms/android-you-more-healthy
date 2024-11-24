package com.edu.fateczl.youmorehealthy.model;

import androidx.annotation.NonNull;

public class MedicAppointment extends Event{

    private String diagnostic;
    private Doctor doctor;

    public MedicAppointment(){
        setType(TaskType.MEDIC);
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " | " + doctor;
    }
}
