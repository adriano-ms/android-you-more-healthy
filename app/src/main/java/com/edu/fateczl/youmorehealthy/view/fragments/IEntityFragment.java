package com.edu.fateczl.youmorehealthy.view.fragments;

public interface IEntityFragment<T> {

    void setEntity(T t);
    T buildEntity();
    void populateFields();
}
