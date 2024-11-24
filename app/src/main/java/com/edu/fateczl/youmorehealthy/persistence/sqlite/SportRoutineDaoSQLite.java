package com.edu.fateczl.youmorehealthy.persistence.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.model.Frequency;
import com.edu.fateczl.youmorehealthy.model.SportRoutine;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.model.Training;
import com.edu.fateczl.youmorehealthy.persistence.ISportRoutineDao;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SportRoutineDaoSQLite implements ISportRoutineDao {

    private final SQLiteOpener sqLiteOpener;
    private SQLiteDatabase database;
    private final RoutineDaoSQLite routineDao;

    public SportRoutineDaoSQLite(SQLiteOpener sqLiteOpener, RoutineDaoSQLite routineDao) {
        this.sqLiteOpener = sqLiteOpener;
        database = sqLiteOpener.getWritableDatabase();
        this.routineDao = routineDao;
    }

    @Override
    public int insert(SportRoutine sportRoutine) throws DatabaseException  {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();
        int id = routineDao.insert(sportRoutine);
        database = sqLiteOpener.getWritableDatabase();
        sqLiteOpener.close();
        return id;
    }

    @Override
    public void update(SportRoutine sportRoutine) throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();
        routineDao.update(sportRoutine);
        database = sqLiteOpener.getWritableDatabase();
        sqLiteOpener.close();
    }

    @Override
    public void delete(int id) throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        database.delete("trainings", "routine_task_id = " + id, null);

        routineDao.delete(id);
        sqLiteOpener.close();
    }

    @SuppressLint("Range")
    @Override
    public SportRoutine findOne(int id) throws DatabaseException, ResourceNotFoundException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        SportRoutine sportRoutine = null;
        String sql =
            "SELECT t.id, t.description, t.task_type, r.hour, r.minute, r.cycle_in_hours, r.repetitions FROM tasks t " +
            "INNER JOIN routines r ON t.id = r.task_id " +
            "WHERE task_type = 'SPORT' AND id = " + id + ";";

        String sqlTrainings =
            "SELECT id, result, training_date, routine_task_id FROM trainings " +
            "WHERE routine_task_id = " + id + ";";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            sportRoutine = new SportRoutine();
            sportRoutine.setFrequency(new Frequency());
            sportRoutine.setId(cursor.getInt(cursor.getColumnIndex("id")));
            sportRoutine.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            sportRoutine.setType(TaskType.valueOf(cursor.getString(cursor.getColumnIndex("task_type"))));
            sportRoutine.getFrequency().setHour(cursor.getInt(cursor.getColumnIndex("hour")));
            sportRoutine.getFrequency().setMinute(cursor.getInt(cursor.getColumnIndex("minute")));
            sportRoutine.getFrequency().setCycleInHours(cursor.getInt(cursor.getColumnIndex("cycle_in_hours")));
            sportRoutine.getFrequency().setRepetitions(cursor.getInt(cursor.getColumnIndex("repetitions")));
            Cursor c = database.rawQuery(sqlTrainings, null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                Training training = new Training();
                training.setId(c.getInt(c.getColumnIndex("id")));
                training.setResult(c.getString(c.getColumnIndex("result")));
                try {
                    training.setDate(SQLiteOpener.DATE_TIME_FORMAT.parse(c.getString(c.getColumnIndex("training_date"))));
                } catch (ParseException e) {
                    throw new DatabaseException(R.string.db_exception_query);
                }
                sportRoutine.addTraining(training);
                c.moveToNext();
            }
            c.close();
            cursor.moveToNext();
        }
        cursor.close();
        if(sportRoutine == null)
            throw new ResourceNotFoundException();

        sqLiteOpener.close();

        return sportRoutine;
    }

    @SuppressLint("Range")
    @Override
    public List<SportRoutine> findAll() throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        List<SportRoutine> routines = new ArrayList<>();
        String sql =
            "SELECT t.id, t.description, t.task_type, r.hour, r.minute, r.cycle_in_hours, r.repetitions FROM tasks t " +
            "INNER JOIN routines r ON t.id = r.task_id " +
            "WHERE task_type = 'SPORT';";

        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            SportRoutine sportRoutine = new SportRoutine();
            sportRoutine.setFrequency(new Frequency());
            sportRoutine.setId(cursor.getInt(cursor.getColumnIndex("id")));
            sportRoutine.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            sportRoutine.setType(TaskType.valueOf(cursor.getString(cursor.getColumnIndex("task_type"))));
            sportRoutine.getFrequency().setHour(cursor.getInt(cursor.getColumnIndex("hour")));
            sportRoutine.getFrequency().setMinute(cursor.getInt(cursor.getColumnIndex("minute")));
            sportRoutine.getFrequency().setCycleInHours(cursor.getInt(cursor.getColumnIndex("cycle_in_hours")));
            sportRoutine.getFrequency().setRepetitions(cursor.getInt(cursor.getColumnIndex("repetitions")));
            String sqlTrainings =
                "SELECT id, result, training_date, routine_task_id FROM trainings " +
                "WHERE routine_task_id = " + sportRoutine.getId() + ";";
            Cursor c = database.rawQuery(sqlTrainings, null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                Training training = new Training();
                training.setId(c.getInt(c.getColumnIndex("id")));
                training.setResult(c.getString(c.getColumnIndex("result")));
                try {
                    training.setDate(SQLiteOpener.DATE_TIME_FORMAT.parse(c.getString(c.getColumnIndex("training_date"))));
                } catch (ParseException e) {
                    throw new DatabaseException(R.string.db_exception_query);
                }
                sportRoutine.addTraining(training);
                c.moveToNext();
            }
            c.close();
            routines.add(sportRoutine);
            cursor.moveToNext();
        }
        cursor.close();

        sqLiteOpener.close();

        return routines;
    }

    @Override
    public int insertTraining(int sportRoutineId, Training training) throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("result", training.getResult());
        contentValues.put("training_date", SQLiteOpener.DATE_TIME_FORMAT.format(training.getDate()));
        contentValues.put("routine_task_id", sportRoutineId);
        long row = database.insert("trainings", null, contentValues);
        if(row < 0)
             throw new DatabaseException(R.string.db_exception_insert);

        sqLiteOpener.close();

        return (int) row;
    }

    @Override
    public void deleteTraining(int id)  throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        long rows = database.delete("trainings", "id = " + id, null);
        if(rows < 1)
            throw new DatabaseException(R.string.db_exception_delete);

        sqLiteOpener.close();
    }

    @Override
    public void updateTraining(Training training) {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("result", training.getResult());
        contentValues.put("training_date", SQLiteOpener.DATE_TIME_FORMAT.format(training.getDate()));
        int rows = database.update("trainings", contentValues, "id = " + training.getId(), null);
        if(rows < 1)
            throw new DatabaseException(R.string.db_exception_update);

        sqLiteOpener.close();
    }
}
