package com.edu.fateczl.youmorehealthy.persistence.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.model.Medicine;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.IMedicineDao;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class MedicineDaoSQLite implements IMedicineDao {

    private final SQLiteOpener sqLiteOpener;
    private SQLiteDatabase database;

    public MedicineDaoSQLite(SQLiteOpener sqLiteOpener) {
        this.sqLiteOpener = sqLiteOpener;
        database = sqLiteOpener.getWritableDatabase();
    }

    @Override
    public int insert(Medicine medicine) throws DatabaseException  {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        ContentValues medicineValues = new ContentValues();
        medicineValues.put("name", medicine.getName());
        medicineValues.put("dose", medicine.getDose());
        long row = database.insert("medicines",null, medicineValues);
        if(row < 0)
            throw new DatabaseException(R.string.db_exception_insert);

        sqLiteOpener.close();

        return (int) row;
    }

    @Override
    public void update(Medicine medicine) throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        ContentValues medicineValues = new ContentValues();
        medicineValues.put("name", medicine.getName());
        medicineValues.put("dose", medicine.getDose());
        int rows = database.update("medicines", medicineValues, "id = " + medicine.getId(), null);
        if(rows < 1)
            throw new DatabaseException(R.string.db_exception_update);

        sqLiteOpener.close();
    }

    @Override
    public void delete(int id) throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        int rows = database.delete("medicines", "id = " + id, null);
        if(rows < 1)
            throw new DatabaseException(R.string.db_exception_delete);

        sqLiteOpener.close();
    }

    @SuppressLint("Range")
    @Override
    public Medicine findOne(int id) throws DatabaseException, ResourceNotFoundException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        Medicine d = null;
        String sql =
            "SELECT id, name, dose FROM medicines " +
            "WHERE id = " + id + ";";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            d = new Medicine();
            d.setId(cursor.getInt(cursor.getColumnIndex("id")));
            d.setName(cursor.getString(cursor.getColumnIndex("name")));
            d.setDose(cursor.getString(cursor.getColumnIndex("dose")));
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
    public List<Medicine> findAll() throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        List<Medicine> medicines = new ArrayList<>();
        String sql =
            "SELECT id, name, dose FROM medicines;";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Medicine d = new Medicine();
            d.setId(cursor.getInt(cursor.getColumnIndex("id")));
            d.setName(cursor.getString(cursor.getColumnIndex("name")));
            d.setDose(cursor.getString(cursor.getColumnIndex("dose")));
            medicines.add(d);
            cursor.moveToNext();
        }
        cursor.close();

        sqLiteOpener.close();

        return medicines;
    }
}
