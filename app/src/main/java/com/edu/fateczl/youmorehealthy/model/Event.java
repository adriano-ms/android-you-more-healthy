package com.edu.fateczl.youmorehealthy.model;

import androidx.annotation.NonNull;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

public class Event extends Task {

    public Date date;

    public Event() {
        setType(TaskType.EVENT);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " " + getDate();
    }
}
