package com.edu.fateczl.youmorehealthy.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.controller.TaskController;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;
import com.edu.fateczl.youmorehealthy.view.listeners.TaskCheckBoxFilterClicked;
import com.edu.fateczl.youmorehealthy.view.listeners.TaskCardClickListener;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

public class ManageTasksFragment extends Fragment {

    private View view;
    private Map<TaskType, CheckBox> taskTypes;
    private RecyclerView rcTasks;
    private FloatingActionButton btNewTask;
    private TaskController controller;

    public ManageTasksFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_manage_tasks, container, false);
        taskTypes = new HashMap<>();
        CheckBox cbEvent = view.findViewById(R.id.cbEvent);
        CheckBox cbRoutine = view.findViewById(R.id.cbRoutine);
        CheckBox cbSport = view.findViewById(R.id.cbSport);
        CheckBox cbMedicine = view.findViewById(R.id.cbMedicine);
        CheckBox cbMedic = view.findViewById(R.id.cbMedic);
        rcTasks = view.findViewById(R.id.rcTasks);
        btNewTask = view.findViewById(R.id.btNewTask);
        controller = new TaskController(view.getContext());
        btNewTask.setOnClickListener(b -> newTaskAction());
        taskTypes.put(TaskType.EVENT, cbEvent);
        taskTypes.put(TaskType.ROUTINE, cbRoutine);
        taskTypes.put(TaskType.SPORT, cbSport);
        taskTypes.put(TaskType.MEDICINE, cbMedicine);
        taskTypes.put(TaskType.MEDIC, cbMedic);
        TaskCardClickListener listener = new TaskCardClickListener(requireActivity().getSupportFragmentManager());
        rcTasks.setLayoutManager(new LinearLayoutManager(view.getContext()));
        TaskCheckBoxFilterClicked cbListener = new TaskCheckBoxFilterClicked(rcTasks, listener, controller, taskTypes);
        taskTypes.forEach((k, v) -> v.setOnClickListener(cbListener));
        cbListener.onClick(cbEvent);
        return view;
    }

    private void newTaskAction(){
        ViewUtils.changeFragment(requireActivity().getSupportFragmentManager(), R.id.taskFragment, new NewTaskFragment(), true);
    }
}