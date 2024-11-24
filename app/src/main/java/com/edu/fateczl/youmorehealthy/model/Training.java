package com.edu.fateczl.youmorehealthy.model;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.Objects;

public class Training {

    private int id;
    private String result;
    private Date date;

    public Training(){
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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
        return getId() + ". " + getDate() + " : " + getResult();
    }

}
