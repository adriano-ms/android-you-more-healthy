package com.edu.fateczl.youmorehealthy.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.controller.ScheduleController;
import com.edu.fateczl.youmorehealthy.controller.TaskController;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;
import com.edu.fateczl.youmorehealthy.view.listeners.NextTaskCheckBoxFilterClicked;
import com.edu.fateczl.youmorehealthy.view.listeners.TaskCardClickListener;
import com.edu.fateczl.youmorehealthy.view.listeners.TaskCheckBoxFilterClicked;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

public class NextTasksFragment extends Fragment {

    private View view;
    private Map<TaskType, CheckBox> taskTypes;
    private RecyclerView rcTasks;
    private TaskController controller;
    private ScheduleController scheduleController;

    public NextTasksFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_next_tasks, container, false);
        taskTypes = new HashMap<>();
        CheckBox cbEvent = view.findViewById(R.id.cbEventNext);
        CheckBox cbRoutine = view.findViewById(R.id.cbRoutineNext);
        CheckBox cbSport = view.findViewById(R.id.cbSportNext);
        CheckBox cbMedicine = view.findViewById(R.id.cbMedicineNext);
        CheckBox cbMedic = view.findViewById(R.id.cbMedicNext);
        rcTasks = view.findViewById(R.id.rcNextTasks);
        controller = new TaskController(view.getContext());
        scheduleController = new ScheduleController(view.getContext());
        taskTypes.put(TaskType.EVENT, cbEvent);
        taskTypes.put(TaskType.ROUTINE, cbRoutine);
        taskTypes.put(TaskType.SPORT, cbSport);
        taskTypes.put(TaskType.MEDICINE, cbMedicine);
        taskTypes.put(TaskType.MEDIC, cbMedic);
        TaskCardClickListener listener = new TaskCardClickListener(requireActivity().getSupportFragmentManager());
        rcTasks.setLayoutManager(new LinearLayoutManager(view.getContext()));
        NextTaskCheckBoxFilterClicked cbListener = new NextTaskCheckBoxFilterClicked(rcTasks, listener, controller, scheduleController, taskTypes);
        taskTypes.forEach((k, v) -> v.setOnClickListener(cbListener));
        cbListener.onClick(cbEvent);
        return view;
    }
}