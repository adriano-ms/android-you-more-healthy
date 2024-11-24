package com.edu.fateczl.youmorehealthy.view.listeners;

import android.view.View;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.edu.fateczl.youmorehealthy.controller.ScheduleController;
import com.edu.fateczl.youmorehealthy.controller.TaskController;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.view.adapters.RVNextTaskAdapter;
import com.edu.fateczl.youmorehealthy.view.adapters.RVTaskAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NextTaskCheckBoxFilterClicked implements View.OnClickListener {

    private final RecyclerView recyclerView;
    private final TaskController controller;
    private final ScheduleController scheduleController;
    private final Map<TaskType, CheckBox> checkBoxMap;
    private final TaskCardClickListener cardClickListener;

    public NextTaskCheckBoxFilterClicked(RecyclerView recyclerView, TaskCardClickListener cardClickListener, TaskController controller, ScheduleController scheduleController, Map<TaskType, CheckBox> checkBoxMap){
        this.recyclerView = recyclerView;
        this.controller = controller;
        this.checkBoxMap = checkBoxMap;
        this.cardClickListener = cardClickListener;
        this.scheduleController = scheduleController;
    }

    @Override
    public void onClick(View v) {
        Map<TaskType, Boolean> types = new HashMap<>();
        types.put(TaskType.EVENT, Objects.requireNonNull(checkBoxMap.get(TaskType.EVENT)).isChecked());
        types.put(TaskType.ROUTINE, Objects.requireNonNull(checkBoxMap.get(TaskType.ROUTINE)).isChecked());
        types.put(TaskType.MEDICINE, Objects.requireNonNull(checkBoxMap.get(TaskType.MEDICINE)).isChecked());
        types.put(TaskType.MEDIC, Objects.requireNonNull(checkBoxMap.get(TaskType.MEDIC)).isChecked());
        types.put(TaskType.SPORT, Objects.requireNonNull(checkBoxMap.get(TaskType.SPORT)).isChecked());
        recyclerView.setAdapter(new RVNextTaskAdapter(controller.listNextTasks(types), cardClickListener, scheduleController));
    }
}
