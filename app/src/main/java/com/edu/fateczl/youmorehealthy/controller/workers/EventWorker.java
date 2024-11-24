package com.edu.fateczl.youmorehealthy.controller.workers;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.persistence.IDaoFactory;
import com.edu.fateczl.youmorehealthy.persistence.IEventDao;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.SQLiteDaoFactory;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;

public class EventWorker extends Worker {

    public EventWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        TaskType taskType = TaskType.valueOf(getInputData().getString("task_type"));
        int id = getInputData().getInt("task_id", 0);
        String description = getInputData().getString("description");
        ViewUtils.showNotification(getApplicationContext(), id,  ViewUtils.chooseTaskIcon(taskType), description, getApplicationContext().getString(R.string.notification_content), new Intent());
        IDaoFactory daoFactory = new SQLiteDaoFactory(getApplicationContext());
        IEventDao eventDao = daoFactory.createEventDao();
        try {
            eventDao.delete(id);
        } catch (DatabaseException e) {
            return Result.failure();
        }
        return Result.success();
    }
}
