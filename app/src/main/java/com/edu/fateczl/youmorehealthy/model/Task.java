package com.edu.fateczl.youmorehealthy.model;

import androidx.annotation.NonNull;

public abstract class Task {

    private int id;
    private String description;
    private TaskType type;

    public Task(){
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        return getId() + ". " + " [" + getType() + "] " + getDescription();
    }
}
