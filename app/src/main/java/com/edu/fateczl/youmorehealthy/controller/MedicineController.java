package com.edu.fateczl.youmorehealthy.controller;

import android.content.Context;

import com.edu.fateczl.youmorehealthy.controller.exceptions.FormException;
import com.edu.fateczl.youmorehealthy.model.Medicine;
import com.edu.fateczl.youmorehealthy.persistence.IDaoFactory;
import com.edu.fateczl.youmorehealthy.persistence.IMedicineDao;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.SQLiteDaoFactory;

import java.util.List;

public class MedicineController {

    private final IMedicineDao medicineDao;

    public MedicineController(Context context){
        IDaoFactory daoFactory = new SQLiteDaoFactory(context);
        medicineDao = daoFactory.createMedicineDao();
    }

    public List<Medicine> listAll() throws DatabaseException {
        return medicineDao.findAll();
    }

    public void saveMedicine(Medicine medicine) throws DatabaseException, FormException {
        if(medicine.getName().isEmpty() || medicine.getDose().isEmpty())
            throw new FormException();

        if(medicine.getId() == 0)
            medicineDao.insert(medicine);
        else
            medicineDao.update(medicine);
    }

    public void deleteMedicine(Medicine medicine) throws DatabaseException {
        medicineDao.delete(medicine.getId());
    }

}
