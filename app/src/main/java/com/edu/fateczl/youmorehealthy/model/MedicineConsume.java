package com.edu.fateczl.youmorehealthy.model;

import androidx.annotation.NonNull;

public class MedicineConsume extends Routine {

    private Medicine medicine;

    public MedicineConsume() {
        setType(TaskType.MEDICINE);
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " | " + getMedicine();
    }
}
