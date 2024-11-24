package com.edu.fateczl.youmorehealthy.controller;

import android.content.Context;

import com.edu.fateczl.youmorehealthy.controller.exceptions.FormException;
import com.edu.fateczl.youmorehealthy.model.Doctor;
import com.edu.fateczl.youmorehealthy.model.MedicAppointment;
import com.edu.fateczl.youmorehealthy.persistence.IDaoFactory;
import com.edu.fateczl.youmorehealthy.persistence.IDoctorDao;
import com.edu.fateczl.youmorehealthy.persistence.IMedicAppointmentDao;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.SQLiteDaoFactory;

import java.util.Date;
import java.util.List;

public class MedicAppointmentController {

    private final IMedicAppointmentDao appointmentDao;
    private final IDoctorDao doctorDao;
    private final ScheduleController scheduleController;

    public MedicAppointmentController(Context context){
        IDaoFactory daoFactory = new SQLiteDaoFactory(context);
        appointmentDao = daoFactory.createMedicAppointmentDao();
        doctorDao = daoFactory.createDoctorDao();
        scheduleController = new ScheduleController(context);
    }

    public MedicAppointment findMedicAppointment(int id) throws DatabaseException, ResourceNotFoundException {
        return appointmentDao.findOne(id);
    }

    public List<Doctor> listDoctors(){
        return doctorDao.findAll();
    }

    public void saveMedicAppointment(MedicAppointment appointment) throws DatabaseException, FormException {
        if(appointment.getDescription().isEmpty())
            throw new FormException();
        if(appointment.getType() == null)
            throw new FormException();
        if(appointment.getDate() == null || appointment.getDate().compareTo(new Date()) < 0)
            throw new FormException();
        if(appointment.getDoctor() == null)
            throw new FormException();

        if(appointment.getId() == 0) {
            int id = appointmentDao.insert(appointment);
            appointment.setId(id);
        } else {
            appointmentDao.update(appointment);
            scheduleController.cancelSchedule(appointment);
        }
        scheduleController.scheduleEvent(appointment);
    }

    public void deleteMedicAppointment(MedicAppointment appointment) throws DatabaseException {
        appointmentDao.delete(appointment.getId());
        scheduleController.cancelSchedule(appointment);
    }

    public void doneMedicAppointment(MedicAppointment appointment) throws DatabaseException {
        appointmentDao.update(appointment);
    }
}
