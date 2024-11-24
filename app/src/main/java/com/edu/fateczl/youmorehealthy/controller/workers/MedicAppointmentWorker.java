package com.edu.fateczl.youmorehealthy.controller.workers;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.view.MainActivity;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;

public class MedicAppointmentWorker extends Worker {

    public MedicAppointmentWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        TaskType taskType = TaskType.valueOf(getInputData().getString("task_type"));
        int id = getInputData().getInt("task_id", 0);
        String description = getInputData().getString("description");
        String content = getApplicationContext().getString(R.string.notification_content_tap);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("task_type", taskType.toString());
        i.putExtra("task_id", id);
        ViewUtils.showNotification(getApplicationContext(), id,  ViewUtils.chooseTaskIcon(taskType), description, content, i);
        return Result.success();
    }
}
