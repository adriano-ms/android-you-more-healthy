package com.edu.fateczl.youmorehealthy.controller.workers;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.controller.ScheduleController;
import com.edu.fateczl.youmorehealthy.model.MedicineConsume;
import com.edu.fateczl.youmorehealthy.model.Routine;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.persistence.IDaoFactory;
import com.edu.fateczl.youmorehealthy.persistence.IMedicineConsumeDao;
import com.edu.fateczl.youmorehealthy.persistence.IRoutineDao;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.SQLiteDaoFactory;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;

public class MedicineConsumeWorker extends Worker {

    public MedicineConsumeWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        TaskType taskType = TaskType.valueOf(getInputData().getString("task_type"));
        int id = getInputData().getInt("task_id", 0);
        IDaoFactory daoFactory = new SQLiteDaoFactory(getApplicationContext());
        IMedicineConsumeDao medicineConsumeDao = daoFactory.createMedicineConsumeDao();
        MedicineConsume r = null;
        try {
            r = medicineConsumeDao.findOne(id);
            r.getFrequency().setRepetitions(r.getFrequency().getRepetitions() - 1);
            String description = getInputData().getString("description") + " : " + r.getMedicine().getName() + " | " + r.getMedicine().getDose();
            String content = getApplicationContext().getString(R.string.notification_content);
            ViewUtils.showNotification(getApplicationContext(), id,  ViewUtils.chooseTaskIcon(taskType), description, content, new Intent());
            if(r.getFrequency().getRepetitions() == 0) {
                ScheduleController controller = new ScheduleController(getApplicationContext());
                controller.cancelSchedule(r);
                medicineConsumeDao.delete(r.getId());
            } else {
                medicineConsumeDao.update(r);
            }
        } catch (DatabaseException | ResourceNotFoundException e) {
            return Result.failure();
        }
        return Result.success();
    }
}
