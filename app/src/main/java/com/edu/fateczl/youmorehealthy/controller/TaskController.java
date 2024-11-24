package com.edu.fateczl.youmorehealthy.controller;

import android.content.Context;

import com.edu.fateczl.youmorehealthy.model.MedicAppointment;
import com.edu.fateczl.youmorehealthy.model.SportRoutine;
import com.edu.fateczl.youmorehealthy.model.Task;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.persistence.IDaoFactory;
import com.edu.fateczl.youmorehealthy.persistence.IEventDao;
import com.edu.fateczl.youmorehealthy.persistence.IMedicAppointmentDao;
import com.edu.fateczl.youmorehealthy.persistence.IMedicineConsumeDao;
import com.edu.fateczl.youmorehealthy.persistence.IRoutineDao;
import com.edu.fateczl.youmorehealthy.persistence.ISportRoutineDao;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.SQLiteDaoFactory;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskController {

    private final IEventDao eventDao;
    private final IRoutineDao routineDao;
    private final IMedicAppointmentDao medicAppointmentDao;
    private final IMedicineConsumeDao medicineConsumeDao;
    private final ISportRoutineDao sportRoutineDao;

    private List<Task> tasks;

    public TaskController(Context context){
        IDaoFactory daoFactory = new SQLiteDaoFactory(context);
        eventDao = daoFactory.createEventDao();
        routineDao = daoFactory.createRoutineDao();
        medicAppointmentDao = daoFactory.createMedicAppointmentDao();
        medicineConsumeDao = daoFactory.createMedicineConsumeDao();
        sportRoutineDao = daoFactory.createSportRoutineDao();
    }

    public List<Task> listAll(){
        tasks = Stream.concat(Stream.concat(Stream.concat(Stream.concat(
                eventDao.findAll().stream(),
                routineDao.findAll().stream()),
                medicAppointmentDao.findAll().stream()),
                sportRoutineDao.findAll().stream()),
                medicineConsumeDao.findAll().stream()).sorted(Comparator.comparingInt(Task::getId)).collect(Collectors.toList());
        return tasks;
    }

    public List<Task> listFiltered(Map<TaskType, Boolean> types){
        if(tasks == null)
            listAll();
        return tasks.stream().filter(t -> types.get(t.getType())).collect(Collectors.toList());
    }

    public List<Task> listNextTasks(Map<TaskType, Boolean> types){
        List<Task> tasks = listFiltered(types);
        return tasks.stream().filter(t -> {
            if(t.getType().equals(TaskType.SPORT))
                return ((SportRoutine)t).getFrequency().getRepetitions() != 0;
            if(t.getType().equals(TaskType.MEDIC))
                return ((MedicAppointment)t).getDate().compareTo(new Date()) > 0;
            return true;
        }).collect(Collectors.toList());
    }
}
