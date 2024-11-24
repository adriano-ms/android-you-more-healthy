package com.edu.fateczl.youmorehealthy.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.view.listeners.TaskTypeSelectedListener;

public class NewTaskFragment extends Fragment {

    private View view;
    private Spinner spTaskTypes;

    public NewTaskFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_task, container, false);
        spTaskTypes = view.findViewById(R.id.spTaskTypes);
        spTaskTypes.setAdapter(new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item,  TaskType.values()));
        spTaskTypes.setOnItemSelectedListener(new TaskTypeSelectedListener(requireActivity().getSupportFragmentManager()));
        return view;
    }
}