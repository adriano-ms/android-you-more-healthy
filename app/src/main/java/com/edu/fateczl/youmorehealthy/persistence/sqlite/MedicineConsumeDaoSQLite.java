package com.edu.fateczl.youmorehealthy.persistence.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.model.Frequency;
import com.edu.fateczl.youmorehealthy.model.Medicine;
import com.edu.fateczl.youmorehealthy.model.MedicineConsume;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.persistence.IMedicineConsumeDao;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class MedicineConsumeDaoSQLite implements IMedicineConsumeDao {

    private final SQLiteOpener sqLiteOpener;
    private SQLiteDatabase database;
    private final RoutineDaoSQLite routineDao;

    public MedicineConsumeDaoSQLite(SQLiteOpener sqLiteOpener, RoutineDaoSQLite routineDao) {
        this.sqLiteOpener = sqLiteOpener;
        database = sqLiteOpener.getWritableDatabase();
        this.routineDao = routineDao;
    }

    @Override
    public int insert(MedicineConsume medicineConsume) throws DatabaseException  {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        int id = routineDao.insert(medicineConsume);
        database = sqLiteOpener.getWritableDatabase();
        ContentValues medicineConsumeValues = new ContentValues();
        medicineConsumeValues.put("routine_task_id", id);
        medicineConsumeValues.put("medicine_id", medicineConsume.getMedicine().getId());
        long row = database.insert("medicine_consumes", null, medicineConsumeValues);
        if(row < 0)
            throw new DatabaseException(R.string.db_exception_insert);

        sqLiteOpener.close();

        return (int) row;
    }

    @Override
    public void update(MedicineConsume medicineConsume) throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        routineDao.update(medicineConsume);
        database = sqLiteOpener.getWritableDatabase();
        ContentValues medicineConsumeValues = new ContentValues();
        medicineConsumeValues.put("medicine_id", medicineConsume.getMedicine().getId());
        int rows = database.update("medicine_consumes", medicineConsumeValues, "routine_task_id = " + medicineConsume.getId(), null);
        if(rows < 1)
            throw new DatabaseException(R.string.db_exception_update);

        sqLiteOpener.close();
    }

    @Override
    public void delete(int id) throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        int rows = database.delete("medicine_consumes", "routine_task_id = " + id, null);
        if(rows < 1)
            throw new DatabaseException(R.string.db_exception_delete);
        routineDao.delete(id);

        sqLiteOpener.close();
    }

    @SuppressLint("Range")
    @Override
    public MedicineConsume findOne(int id) throws DatabaseException, ResourceNotFoundException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        MedicineConsume medicineConsume = null;
        String sql =
            "SELECT t.id, t.description, t.task_type, r.hour, r.minute, r.cycle_in_hours, r.repetitions, c.medicine_id, m.name, m.dose FROM tasks t " +
            "INNER JOIN routines r ON t.id = r.task_id " +
            "INNER JOIN medicine_consumes c ON c.routine_task_id = r.task_id " +
            "INNER JOIN medicines m ON c.medicine_id = m.id " +
            "WHERE task_type = 'MEDICINE' AND t.id = " + id + ";";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            medicineConsume = new MedicineConsume();
            medicineConsume.setFrequency(new Frequency());
            medicineConsume.setId(cursor.getInt(cursor.getColumnIndex("id")));
            medicineConsume.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            medicineConsume.setType(TaskType.valueOf(cursor.getString(cursor.getColumnIndex("task_type"))));
            medicineConsume.getFrequency().setHour(cursor.getInt(cursor.getColumnIndex("hour")));
            medicineConsume.getFrequency().setMinute(cursor.getInt(cursor.getColumnIndex("minute")));
            medicineConsume.getFrequency().setCycleInHours(cursor.getInt(cursor.getColumnIndex("cycle_in_hours")));
            medicineConsume.getFrequency().setRepetitions(cursor.getInt(cursor.getColumnIndex("repetitions")));
            Medicine medicine = new Medicine();
            medicine.setId(cursor.getInt(cursor.getColumnIndex("medicine_id")));
            medicine.setName(cursor.getString(cursor.getColumnIndex("name")));
            medicine.setDose(cursor.getString(cursor.getColumnIndex("dose")));
            medicineConsume.setMedicine(medicine);
            cursor.moveToNext();
        }
        cursor.close();
        if(medicineConsume == null)
            throw new ResourceNotFoundException();

        sqLiteOpener.close();

        return medicineConsume;
    }

    @SuppressLint("Range")
    @Override
    public List<MedicineConsume> findAll() throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        List<MedicineConsume> medicineConsumes = new ArrayList<>();
        String sql =
            "SELECT t.id, t.description, t.task_type, r.hour, r.minute, r.cycle_in_hours, r.repetitions, c.medicine_id, m.name, m.dose FROM tasks t " +
            "INNER JOIN routines r ON t.id = r.task_id " +
            "INNER JOIN medicine_consumes c ON c.routine_task_id = r.task_id " +
            "INNER JOIN medicines m ON c.medicine_id = m.id " +
            "WHERE task_type = 'MEDICINE';";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            MedicineConsume medicineConsume = new MedicineConsume();
            medicineConsume.setFrequency(new Frequency());
            medicineConsume.setId(cursor.getInt(cursor.getColumnIndex("id")));
            medicineConsume.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            medicineConsume.setType(TaskType.valueOf(cursor.getString(cursor.getColumnIndex("task_type"))));
            medicineConsume.getFrequency().setHour(cursor.getInt(cursor.getColumnIndex("hour")));
            medicineConsume.getFrequency().setMinute(cursor.getInt(cursor.getColumnIndex("minute")));
            medicineConsume.getFrequency().setCycleInHours(cursor.getInt(cursor.getColumnIndex("cycle_in_hours")));
            medicineConsume.getFrequency().setRepetitions(cursor.getInt(cursor.getColumnIndex("repetitions")));
            Medicine medicine = new Medicine();
            medicine.setId(cursor.getInt(cursor.getColumnIndex("medicine_id")));
            medicine.setName(cursor.getString(cursor.getColumnIndex("name")));
            medicine.setDose(cursor.getString(cursor.getColumnIndex("dose")));
            medicineConsume.setMedicine(medicine);
            medicineConsumes.add(medicineConsume);
            cursor.moveToNext();
        }
        cursor.close();

        sqLiteOpener.close();

        return medicineConsumes;
    }
}
