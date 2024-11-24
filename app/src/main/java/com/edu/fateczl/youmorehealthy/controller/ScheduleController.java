package com.edu.fateczl.youmorehealthy.controller;

import android.content.Context;
import android.util.Log;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;

import com.edu.fateczl.youmorehealthy.controller.exceptions.ScheduleException;
import com.edu.fateczl.youmorehealthy.controller.workers.EventWorker;
import com.edu.fateczl.youmorehealthy.controller.workers.MedicAppointmentWorker;
import com.edu.fateczl.youmorehealthy.controller.workers.MedicineConsumeWorker;
import com.edu.fateczl.youmorehealthy.controller.workers.RoutineWorker;
import com.edu.fateczl.youmorehealthy.controller.workers.SportRoutineWorker;
import com.edu.fateczl.youmorehealthy.model.Event;
import com.edu.fateczl.youmorehealthy.model.Routine;
import com.edu.fateczl.youmorehealthy.model.Task;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.view.ViewUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ScheduleController {

    private final WorkManager workManager;

    public ScheduleController(Context context){
        workManager = WorkManager.getInstance(context);
    }

    public void scheduleEvent(Event event){
        long delay = event.getDate().getTime() - System.currentTimeMillis();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(chooseWorker(event.getType()))
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(buildData(event))
                .addTag(String.valueOf(event.getId()))
                .build();
        workManager.enqueue(workRequest);
    }

    public void scheduleRoutine(Routine routine){
        Date now = new Date();
        long delay = getRoutineFirstDate(routine, now).getTime() - now.getTime();
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(chooseWorker(routine.getType()), routine.getFrequency().getCycleInHours(), TimeUnit.HOURS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(buildData(routine))
                .addTag(String.valueOf(routine.getId()))
                .build();
        workManager.enqueue(workRequest);
    }

    public Date getRoutineFirstDate(Routine routine, Date now){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, routine.getFrequency().getHour());
        c.set(Calendar.MINUTE, routine.getFrequency().getMinute());
        c.set(Calendar.SECOND, 0);
        while (c.getTime().compareTo(now) <= 0)
            c.add(Calendar.HOUR_OF_DAY, routine.getFrequency().getCycleInHours());
        return c.getTime();
    }

    public void cancelSchedule(Task task){
        workManager.cancelAllWorkByTag(String.valueOf(task.getId()));
    }

    private Data buildData(Task task){
        return new Data.Builder()
                .putString("task_type", task.getType().toString())
                .putInt("task_id", task.getId())
                .putString("description", task.getDescription()).build();
    }

    private Class<? extends Worker> chooseWorker(TaskType type){
        switch (type){
            case ROUTINE:
                return RoutineWorker.class;
            case MEDICINE:
                return MedicineConsumeWorker.class;
            case MEDIC:
                return MedicAppointmentWorker.class;
            case SPORT:
                return SportRoutineWorker.class;
            default:
                return EventWorker.class;
        }
    }
}
