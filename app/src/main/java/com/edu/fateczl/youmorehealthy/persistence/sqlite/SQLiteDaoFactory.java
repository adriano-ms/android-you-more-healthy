package com.edu.fateczl.youmorehealthy.persistence.sqlite;

import android.content.Context;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.model.Task;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.persistence.IDao;
import com.edu.fateczl.youmorehealthy.persistence.IDaoFactory;
import com.edu.fateczl.youmorehealthy.persistence.IDoctorDao;
import com.edu.fateczl.youmorehealthy.persistence.IEventDao;
import com.edu.fateczl.youmorehealthy.persistence.IMedicAppointmentDao;
import com.edu.fateczl.youmorehealthy.persistence.IMedicineConsumeDao;
import com.edu.fateczl.youmorehealthy.persistence.IMedicineDao;
import com.edu.fateczl.youmorehealthy.persistence.IRoutineDao;
import com.edu.fateczl.youmorehealthy.persistence.ISportRoutineDao;
import com.edu.fateczl.youmorehealthy.view.fragments.EventFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.MedicAppointmentFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.MedicineConsumeFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.RoutineFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.SportRoutineFragment;

public class SQLiteDaoFactory implements IDaoFactory {

    private final SQLiteOpener sqLiteOpener;

    public SQLiteDaoFactory(Context context){
        sqLiteOpener = new SQLiteOpener(context);
    }

    @Override
    public IEventDao createEventDao(){
        return new EventDaoSQLite(sqLiteOpener);
    }

    @Override
    public IRoutineDao createRoutineDao(){
        return new RoutineDaoSQLite(sqLiteOpener);
    }

    @Override
    public IMedicAppointmentDao createMedicAppointmentDao(){
        return new MedicAppointmentDaoSQLite(sqLiteOpener, (EventDaoSQLite) createEventDao());
    }

    @Override
    public IMedicineConsumeDao createMedicineConsumeDao(){
        return new MedicineConsumeDaoSQLite(sqLiteOpener, (RoutineDaoSQLite) createRoutineDao());
    }

    @Override
    public ISportRoutineDao createSportRoutineDao(){
        return new SportRoutineDaoSQLite(sqLiteOpener, (RoutineDaoSQLite) createRoutineDao());
    }

    @Override
    public IDoctorDao createDoctorDao(){
        return new DoctorDaoSQLite(sqLiteOpener);
    }

    @Override
    public IMedicineDao createMedicineDao(){
        return new MedicineDaoSQLite(sqLiteOpener);
    }

}
