package com.edu.fateczl.youmorehealthy.controller;

import android.content.Context;

import com.edu.fateczl.youmorehealthy.controller.exceptions.FormException;
import com.edu.fateczl.youmorehealthy.model.Event;
import com.edu.fateczl.youmorehealthy.persistence.IDaoFactory;
import com.edu.fateczl.youmorehealthy.persistence.IEventDao;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.SQLiteDaoFactory;

import java.util.ConcurrentModificationException;
import java.util.Date;

public class EventController {

    private final IEventDao eventDao;
    private final ScheduleController scheduleController;

    public EventController(Context context){
        IDaoFactory daoFactory = new SQLiteDaoFactory(context);
        eventDao = daoFactory.createEventDao();
        scheduleController = new ScheduleController(context);
    }

    public void saveEvent(Event event) throws DatabaseException, FormException {
        if(event.getDescription().isEmpty())
            throw new FormException();
        if(event.getType() == null)
            throw new FormException();
        if(event.getDate() == null || event.getDate().compareTo(new Date()) < 0)
            throw new FormException();

        if(event.getId() == 0) {
            int id = eventDao.insert(event);
            event.setId(id);
        } else {
            eventDao.update(event);
            scheduleController.cancelSchedule(event);
        }
        scheduleController.scheduleEvent(event);
    }

    public void deleteEvent(Event event) throws DatabaseException {
        eventDao.delete(event.getId());
        scheduleController.cancelSchedule(event);
    }
}
