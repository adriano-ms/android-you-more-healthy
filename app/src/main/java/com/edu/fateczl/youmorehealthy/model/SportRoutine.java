package com.edu.fateczl.youmorehealthy.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class SportRoutine extends Routine {

    private final List<Training> trainings;

    public SportRoutine(){
        setType(TaskType.SPORT);
        trainings = new ArrayList<>();
    }

    public void addTraining(Training training){
        trainings.add(training);
    }

    public void removeTraining(Training training){
        trainings.remove(training);
    }

    public List<Training> getTrainings(){
        return new ArrayList<>(trainings);
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " | " + trainings.size() + " trainings";
    }
}
