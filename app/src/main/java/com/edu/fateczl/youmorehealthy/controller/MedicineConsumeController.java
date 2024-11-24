package com.edu.fateczl.youmorehealthy.controller;

import android.content.Context;

import com.edu.fateczl.youmorehealthy.controller.exceptions.FormException;
import com.edu.fateczl.youmorehealthy.model.Medicine;
import com.edu.fateczl.youmorehealthy.model.MedicineConsume;
import com.edu.fateczl.youmorehealthy.persistence.IDaoFactory;
import com.edu.fateczl.youmorehealthy.persistence.IMedicineConsumeDao;
import com.edu.fateczl.youmorehealthy.persistence.IMedicineDao;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.SQLiteDaoFactory;

import java.util.List;

public class MedicineConsumeController {

    private final IMedicineConsumeDao medicineConsumeDao;
    private final IMedicineDao medicineDao;
    private final ScheduleController scheduleController;

    public MedicineConsumeController(Context context){
        IDaoFactory daoFactory = new SQLiteDaoFactory(context);
        medicineConsumeDao = daoFactory.createMedicineConsumeDao();
        medicineDao = daoFactory.createMedicineDao();
        scheduleController = new ScheduleController(context);
    }

    public List<Medicine> listMedicines(){
        return medicineDao.findAll();
    }

    public void saveMedicineConsume(MedicineConsume medicineConsume) throws DatabaseException, FormException {
        if(medicineConsume.getDescription().isEmpty())
            throw new FormException();
        if(medicineConsume.getType() == null)
            throw new FormException();
        if(medicineConsume.getFrequency() == null)
            throw new FormException();
        if(medicineConsume.getFrequency().getHour() > 23 || medicineConsume.getFrequency().getHour() < 0)
            throw new FormException();
        if(medicineConsume.getFrequency().getMinute() > 59 || medicineConsume.getFrequency().getMinute() < 0)
            throw new FormException();
        if(medicineConsume.getFrequency().getCycleInHours() < 1)
            throw new FormException();
        if(medicineConsume.getFrequency().getRepetitions() < 1)
            throw new FormException();
        if(medicineConsume.getMedicine() == null)
            throw new FormException();

        if(medicineConsume.getId() == 0) {
            int id = medicineConsumeDao.insert(medicineConsume);
            medicineConsume.setId(id);
        } else {
            medicineConsumeDao.update(medicineConsume);
            scheduleController.cancelSchedule(medicineConsume);
        }
        scheduleController.scheduleRoutine(medicineConsume);
    }

    public void deleteMedicineConsume(MedicineConsume medicineConsume) throws DatabaseException {
        medicineConsumeDao.delete(medicineConsume.getId());
        scheduleController.cancelSchedule(medicineConsume);
    }
    
}
