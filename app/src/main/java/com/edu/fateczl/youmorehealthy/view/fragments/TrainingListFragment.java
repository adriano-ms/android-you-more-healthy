package com.edu.fateczl.youmorehealthy.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.controller.SportRoutineController;
import com.edu.fateczl.youmorehealthy.model.SportRoutine;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;
import com.edu.fateczl.youmorehealthy.view.adapters.RVTrainingAdapter;
import com.edu.fateczl.youmorehealthy.view.fragments.factories.FragmentFactory;
import com.edu.fateczl.youmorehealthy.view.listeners.TrainingCardClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TrainingListFragment extends Fragment implements IEntityFragment<SportRoutine> {

    private View view;
    private TextView textView;
    private RecyclerView rvTrainings;
    private FloatingActionButton btNewTraining;
    private SportRoutine sportRoutine;
    private SportRoutineController controller;

    public TrainingListFragment(SportRoutine sportRoutine){
        this.sportRoutine = sportRoutine;
    }

    public TrainingListFragment(int sportRoutineId){
        sportRoutine = new SportRoutine();
        sportRoutine.setId(sportRoutineId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_training_list, container, false);
        textView = view.findViewById(R.id.tvSportRoutineTitle);
        rvTrainings = view.findViewById(R.id.rvTrainings);
        btNewTraining = view.findViewById(R.id.btNewTraining);
        btNewTraining.setOnClickListener(b -> newTrainingAction());
        rvTrainings.setLayoutManager(new LinearLayoutManager(view.getContext()));
        controller = new SportRoutineController(view.getContext());
        populateFields();
        return view;
    }

    private void newTrainingAction(){
        ViewUtils.changeFragment(requireActivity().getSupportFragmentManager(), R.id.taskFragment, FragmentFactory.createTrainingFragment(sportRoutine), true);
    }

    @Override
    public void setEntity(SportRoutine sportRoutine) {
        this.sportRoutine = sportRoutine;
    }

    @Override
    public SportRoutine buildEntity() {
        return sportRoutine;
    }

    @Override
    public void populateFields() {
        if(sportRoutine.getDescription() == null)
            sportRoutine = controller.findRoutine(sportRoutine.getId());
        textView.setText(sportRoutine.getDescription());
        rvTrainings.setAdapter(new RVTrainingAdapter(sportRoutine.getTrainings(), new TrainingCardClickListener(requireActivity().getSupportFragmentManager(), sportRoutine)));
    }
}