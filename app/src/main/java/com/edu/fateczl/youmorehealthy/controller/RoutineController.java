package com.edu.fateczl.youmorehealthy.controller;

import android.content.Context;

import com.edu.fateczl.youmorehealthy.controller.exceptions.FormException;
import com.edu.fateczl.youmorehealthy.model.Routine;
import com.edu.fateczl.youmorehealthy.persistence.IDaoFactory;
import com.edu.fateczl.youmorehealthy.persistence.IRoutineDao;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.SQLiteDaoFactory;

import java.util.Date;

public class RoutineController {

    private final IRoutineDao routineDao;
    private final ScheduleController scheduleController;

    public RoutineController(Context context){
        IDaoFactory daoFactory = new SQLiteDaoFactory(context);
        routineDao = daoFactory.createRoutineDao();
        scheduleController = new ScheduleController(context);
    }

    public void saveRoutine(Routine routine) throws DatabaseException, FormException {
        if(routine.getDescription().isEmpty())
            throw new FormException();
        if(routine.getType() == null)
            throw new FormException();
        if(routine.getFrequency() == null)
            throw new FormException();
        if(routine.getFrequency().getHour() > 23 || routine.getFrequency().getHour() < 0)
            throw new FormException();
        if(routine.getFrequency().getMinute() > 59 || routine.getFrequency().getMinute() < 0)
            throw new FormException();
        if(routine.getFrequency().getCycleInHours() < 1)
            throw new FormException();
        if(routine.getFrequency().getRepetitions() < 1)
            throw new FormException();

        if(routine.getId() == 0) {
            int id = routineDao.insert(routine);
            routine.setId(id);
        } else {
            routineDao.update(routine);
            scheduleController.cancelSchedule(routine);
        }
        scheduleController.scheduleRoutine(routine);
    }

    public void deleteRoutine(Routine routine) throws DatabaseException {
        routineDao.delete(routine.getId());
        scheduleController.cancelSchedule(routine);
    }
}
