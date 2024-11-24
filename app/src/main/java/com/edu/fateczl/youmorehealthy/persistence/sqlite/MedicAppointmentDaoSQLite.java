package com.edu.fateczl.youmorehealthy.persistence.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.model.Doctor;
import com.edu.fateczl.youmorehealthy.model.MedicAppointment;
import com.edu.fateczl.youmorehealthy.persistence.IMedicAppointmentDao;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MedicAppointmentDaoSQLite implements IMedicAppointmentDao {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", new Locale("pt", "BR"));

    private final SQLiteOpener sqLiteOpener;
    private SQLiteDatabase database;
    private final EventDaoSQLite eventDao;

    public MedicAppointmentDaoSQLite(SQLiteOpener sqLiteOpener, EventDaoSQLite eventDao) {
        this.sqLiteOpener = sqLiteOpener;
        database = sqLiteOpener.getWritableDatabase();
        this.eventDao = eventDao;
    }

    @Override
    public int insert(MedicAppointment medicAppointment) throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        int id = eventDao.insert(medicAppointment);
        database = sqLiteOpener.getWritableDatabase();
        ContentValues medicValues = new ContentValues();
        medicValues.put("event_task_id", id);
        medicValues.put("diagnostic", medicAppointment.getDiagnostic());
        medicValues.put("doctor_id", medicAppointment.getDoctor().getId());
        long row = database.insert("medic_appointments", null, medicValues);
        if(row < 0)
            throw new DatabaseException(R.string.db_exception_insert);

        sqLiteOpener.close();

        return (int) row;
    }

    @Override
    public void update(MedicAppointment medicAppointment) throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        eventDao.update(medicAppointment);
        database = sqLiteOpener.getWritableDatabase();
        ContentValues medicValues = new ContentValues();
        medicValues.put("diagnostic", medicAppointment.getDiagnostic());
        medicValues.put("doctor_id", medicAppointment.getDoctor().getId());
        long rows = database.update("medic_appointments", medicValues, "event_task_id = " + medicAppointment.getId(), null);
        if(rows < 1)
            throw new DatabaseException(R.string.db_exception_update);

        sqLiteOpener.close();
    }

    @Override
    public void delete(int id) throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        long rows = database.delete("medic_appointments", "event_task_id = " + id, null);
        if(rows < 1)
            throw new DatabaseException(R.string.db_exception_delete);
        eventDao.delete(id);

        sqLiteOpener.close();
    }

    @SuppressLint("Range")
    @Override
    public MedicAppointment findOne(int id) throws DatabaseException, ResourceNotFoundException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        MedicAppointment m = null;
        String sql =
            "SELECT t.id, t.description, t.task_type, e.event_date, m.diagnostic, m.doctor_id, d.name, d.speciality FROM tasks t " +
            "INNER JOIN events e ON t.id = e.task_id " +
            "INNER JOIN medic_appointments m ON e.task_id = m.event_task_id " +
            "INNER JOIN doctors d ON m.doctor_id = d.id " +
            "WHERE task_type = 'MEDIC' AND t.id = " + id + ";";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            m = new MedicAppointment();
            m.setId(cursor.getInt(cursor.getColumnIndex("id")));
            m.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            try {
                m.setDate(SIMPLE_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex("event_date"))));
            } catch (ParseException e) {
                throw new DatabaseException(R.string.db_exception_query);
            }
            m.setDiagnostic(cursor.getString(cursor.getColumnIndex("diagnostic")));
            Doctor d = new Doctor();
            d.setId(cursor.getInt(cursor.getColumnIndex("doctor_id")));
            d.setName(cursor.getString(cursor.getColumnIndex("name")));
            d.setSpeciality(cursor.getString(cursor.getColumnIndex("speciality")));
            m.setDoctor(d);
            cursor.moveToNext();
        }
        cursor.close();
        if(m == null)
            throw new ResourceNotFoundException();

        sqLiteOpener.close();

        return m;
    }

    @SuppressLint("Range")
    @Override
    public List<MedicAppointment> findAll() throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        List<MedicAppointment> appointments = new ArrayList<>();
        String sql =
            "SELECT t.id, t.description, t.task_type, e.event_date, m.diagnostic, m.doctor_id, d.name, d.speciality FROM tasks t " +
            "INNER JOIN events e ON t.id = e.task_id " +
            "INNER JOIN medic_appointments m ON e.task_id = m.event_task_id " +
            "INNER JOIN doctors d ON m.doctor_id = d.id " +
            "WHERE task_type = 'MEDIC';";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            MedicAppointment m = new MedicAppointment();
            m.setId(cursor.getInt(cursor.getColumnIndex("id")));
            m.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            try {
                m.setDate(SIMPLE_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex("event_date"))));
            } catch (ParseException e) {
                throw new DatabaseException(R.string.db_exception_query);
            }
            m.setDiagnostic(cursor.getString(cursor.getColumnIndex("diagnostic")));
            Doctor d = new Doctor();
            d.setId(cursor.getInt(cursor.getColumnIndex("doctor_id")));
            d.setName(cursor.getString(cursor.getColumnIndex("name")));
            d.setSpeciality(cursor.getString(cursor.getColumnIndex("speciality")));
            m.setDoctor(d);
            appointments.add(m);
            cursor.moveToNext();
        }
        cursor.close();

        sqLiteOpener.close();

        return appointments;
    }
}
