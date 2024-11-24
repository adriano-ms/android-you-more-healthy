package com.edu.fateczl.youmorehealthy.controller.workers;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.controller.ScheduleController;
import com.edu.fateczl.youmorehealthy.model.Routine;
import com.edu.fateczl.youmorehealthy.model.Task;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.persistence.IDao;
import com.edu.fateczl.youmorehealthy.persistence.IDaoFactory;
import com.edu.fateczl.youmorehealthy.persistence.IRoutineDao;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.SQLiteDaoFactory;
import com.edu.fateczl.youmorehealthy.view.MainActivity;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;

public class RoutineWorker extends Worker {

    public RoutineWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        TaskType taskType = TaskType.valueOf(getInputData().getString("task_type"));
        int id = getInputData().getInt("task_id", 0);
        String description = getInputData().getString("description");
        String content = getApplicationContext().getString(R.string.notification_content);
        ViewUtils.showNotification(getApplicationContext(), id,  ViewUtils.chooseTaskIcon(taskType), description, content, new Intent());
        IDaoFactory daoFactory = new SQLiteDaoFactory(getApplicationContext());
        IRoutineDao routineDao = daoFactory.createRoutineDao();
        Routine r = null;
        try {
            r = routineDao.findOne(id);
            r.getFrequency().setRepetitions(r.getFrequency().getRepetitions() - 1);
            if(r.getFrequency().getRepetitions() == 0) {
                ScheduleController controller = new ScheduleController(getApplicationContext());
                controller.cancelSchedule(r);
                routineDao.delete(r.getId());
            } else {
                routineDao.update(r);
            }
        } catch (DatabaseException | ResourceNotFoundException e) {
            return Result.failure();
        }
        return Result.success();
    }
}
