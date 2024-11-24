package com.edu.fateczl.youmorehealthy.persistence;

import com.edu.fateczl.youmorehealthy.model.Task;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.persistence.sqlite.EventDaoSQLite;

public interface IDaoFactory {

    IEventDao createEventDao();
    IRoutineDao createRoutineDao();
    IMedicAppointmentDao createMedicAppointmentDao();
    IMedicineConsumeDao createMedicineConsumeDao();
    ISportRoutineDao createSportRoutineDao();
    IDoctorDao createDoctorDao();
    IMedicineDao createMedicineDao();

}
