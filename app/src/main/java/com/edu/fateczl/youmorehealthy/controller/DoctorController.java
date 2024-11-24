package com.edu.fateczl.youmorehealthy.controller;

import android.content.Context;

import com.edu.fateczl.youmorehealthy.controller.exceptions.FormException;
import com.edu.fateczl.youmorehealthy.model.Doctor;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.IDaoFactory;
import com.edu.fateczl.youmorehealthy.persistence.IDoctorDao;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.SQLiteDaoFactory;

import java.util.List;

public class DoctorController {

    private final IDoctorDao doctorDao;

    public DoctorController(Context context){
        IDaoFactory daoFactory = new SQLiteDaoFactory(context);
        doctorDao = daoFactory.createDoctorDao();
    }

    public List<Doctor> listAll() throws DatabaseException {
        return doctorDao.findAll();
    }

    public void saveDoctor(Doctor doctor) throws DatabaseException, FormException {
        if(doctor.getName().isEmpty() || doctor.getSpeciality().isEmpty())
            throw new FormException();

        if(doctor.getId() == 0)
            doctorDao.insert(doctor);
        else
            doctorDao.update(doctor);
    }

    public void deleteDoctor(Doctor doctor) throws DatabaseException {
        doctorDao.delete(doctor.getId());
    }
}
