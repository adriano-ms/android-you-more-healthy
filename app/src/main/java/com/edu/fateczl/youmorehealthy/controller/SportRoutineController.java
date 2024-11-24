package com.edu.fateczl.youmorehealthy.controller;

import android.content.Context;

import com.edu.fateczl.youmorehealthy.controller.exceptions.FormException;
import com.edu.fateczl.youmorehealthy.model.SportRoutine;
import com.edu.fateczl.youmorehealthy.model.Training;
import com.edu.fateczl.youmorehealthy.persistence.IDaoFactory;
import com.edu.fateczl.youmorehealthy.persistence.ISportRoutineDao;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.SQLiteDaoFactory;

import java.util.Date;

public class SportRoutineController {

    private final ISportRoutineDao sportRoutineDao;
    private final ScheduleController scheduleController;

    public SportRoutineController(Context context){
        IDaoFactory daoFactory = new SQLiteDaoFactory(context);
        sportRoutineDao = daoFactory.createSportRoutineDao();
        scheduleController = new ScheduleController(context);
    }

    public void saveRoutine(SportRoutine sportRoutine) throws DatabaseException, FormException {
        if(sportRoutine.getDescription().isEmpty())
            throw new FormException();
        if(sportRoutine.getType() == null)
            throw new FormException();
        if(sportRoutine.getFrequency() == null)
            throw new FormException();
        if(sportRoutine.getFrequency().getHour() > 23 || sportRoutine.getFrequency().getHour() < 0)
            throw new FormException();
        if(sportRoutine.getFrequency().getMinute() > 59 || sportRoutine.getFrequency().getMinute() < 0)
            throw new FormException();
        if(sportRoutine.getFrequency().getCycleInHours() < 1)
            throw new FormException();
        if(sportRoutine.getFrequency().getRepetitions() < 1)
            throw new FormException();

        if(sportRoutine.getId() == 0) {
            int id = sportRoutineDao.insert(sportRoutine);
            sportRoutine.setId(id);
        } else {
            sportRoutineDao.update(sportRoutine);
            scheduleController.cancelSchedule(sportRoutine);
        }
        scheduleController.scheduleRoutine(sportRoutine);
    }

    public void deleteRoutine(SportRoutine sportRoutine) throws DatabaseException {
        sportRoutineDao.delete(sportRoutine.getId());
        scheduleController.cancelSchedule(sportRoutine);
    }

    public SportRoutine findRoutine(int id) throws DatabaseException, ResourceNotFoundException{
        return sportRoutineDao.findOne(id);
    }

    public void addTraining(int sportRoutineId, Training training) throws DatabaseException, FormException {
        if(training.getDate() == null)
            throw new FormException();
        if(training.getDate().compareTo(new Date()) > 0)
            throw new FormException();
        if(training.getResult().isEmpty())
            throw new FormException();

        if(training.getId() == 0) {
            int id = sportRoutineDao.insertTraining(sportRoutineId, training);
            training.setId(id);
        } else
            sportRoutineDao.updateTraining(training);
    }

    public void deleteTraining(int trainingId) throws DatabaseException {
        sportRoutineDao.deleteTraining(trainingId);
    }

}
