package com.edu.fateczl.youmorehealthy.persistence.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.model.Doctor;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.IDoctorDao;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class DoctorDaoSQLite implements IDoctorDao {

    private final SQLiteOpener sqLiteOpener;
    private SQLiteDatabase database;

    public DoctorDaoSQLite(SQLiteOpener sqLiteOpener) {
        this.sqLiteOpener = sqLiteOpener;
        database = sqLiteOpener.getWritableDatabase();
    }

    @Override
    public int insert(Doctor doctor) throws DatabaseException  {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        ContentValues doctorValues = new ContentValues();
        doctorValues.put("name", doctor.getName());
        doctorValues.put("speciality", doctor.getSpeciality());
        long row = database.insert("doctors",null, doctorValues);
        if(row < 0)
            throw new DatabaseException(R.string.db_exception_insert);

        sqLiteOpener.close();

        return (int) row;
    }

    @Override
    public void update(Doctor doctor) throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        ContentValues doctorValues = new ContentValues();
        doctorValues.put("name", doctor.getName());
        doctorValues.put("speciality", doctor.getSpeciality());
        int rows = database.update("doctors", doctorValues, "id = " + doctor.getId(), null);
        if(rows < 1)
            throw new DatabaseException(R.string.db_exception_update);

        sqLiteOpener.close();
    }

    @Override
    public void delete(int id) throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        int rows = database.delete("doctors", "id = " + id, null);
        if(rows < 1)
            throw new DatabaseException(R.string.db_exception_delete);

        sqLiteOpener.close();
    }

    @SuppressLint("Range")
    @Override
    public Doctor findOne(int id) throws DatabaseException, ResourceNotFoundException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        Doctor d = null;
        String sql =
            "SELECT id, name, speciality FROM doctors " +
            "WHERE id = " + id + ";";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            d = new Doctor();
            d.setId(cursor.getInt(cursor.getColumnIndex("id")));
            d.setName(cursor.getString(cursor.getColumnIndex("name")));
            d.setSpeciality(cursor.getString(cursor.getColumnIndex("speciality")));
            cursor.moveToNext();
        }
        cursor.close();
        if(d == null)
            throw new ResourceNotFoundException();

        sqLiteOpener.close();

        return d;
    }

    @SuppressLint("Range")
    @Override
    public List<Doctor> findAll() throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        List<Doctor> doctors = new ArrayList<>();
        String sql =
            "SELECT id, name, speciality FROM doctors;";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Doctor d = new Doctor();
            d.setId(cursor.getInt(cursor.getColumnIndex("id")));
            d.setName(cursor.getString(cursor.getColumnIndex("name")));
            d.setSpeciality(cursor.getString(cursor.getColumnIndex("speciality")));
            doctors.add(d);
            cursor.moveToNext();
        }
        cursor.close();

        sqLiteOpener.close();

        return doctors;
    }
}
