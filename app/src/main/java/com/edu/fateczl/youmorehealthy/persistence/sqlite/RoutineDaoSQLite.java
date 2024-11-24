package com.edu.fateczl.youmorehealthy.persistence.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.model.Frequency;
import com.edu.fateczl.youmorehealthy.model.Routine;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.IRoutineDao;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class RoutineDaoSQLite implements IRoutineDao {

    private final SQLiteOpener sqLiteOpener;
    private SQLiteDatabase database;

    public RoutineDaoSQLite(SQLiteOpener sqLiteOpener) {
        this.sqLiteOpener = sqLiteOpener;
        database = sqLiteOpener.getWritableDatabase();
    }

    @Override
    public int insert(Routine routine) throws DatabaseException  {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        ContentValues taskValues = new ContentValues();
        taskValues.put("description", routine.getDescription());
        taskValues.put("task_type", routine.getType().toString());
        long taskRow = database.insert("tasks",null, taskValues);
        if(taskRow < 0)
            throw new DatabaseException(R.string.db_exception_insert);
        ContentValues routineValues = new ContentValues();
        routineValues.put("task_id", taskRow);
        routineValues.put("hour", routine.getFrequency().getHour());
        routineValues.put("minute", routine.getFrequency().getMinute());
        routineValues.put("cycle_in_hours", routine.getFrequency().getCycleInHours());
        routineValues.put("repetitions", routine.getFrequency().getRepetitions());
        long row = database.insert("routines", null, routineValues);
        if(row < 0)
            throw new DatabaseException(R.string.db_exception_insert);

        sqLiteOpener.close();

        return (int) row;
    }

    @Override
    public void update(Routine routine) throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        ContentValues taskValues = new ContentValues();
        taskValues.put("description", routine.getDescription());
        taskValues.put("task_type", routine.getType().toString());
        int taskRows = database.update("tasks", taskValues, "id = " + routine.getId(), null);
        if(taskRows < 1)
            throw new DatabaseException(R.string.db_exception_update);
        ContentValues routineValues = new ContentValues();
        routineValues.put("hour", routine.getFrequency().getHour());
        routineValues.put("minute", routine.getFrequency().getMinute());
        routineValues.put("cycle_in_hours", routine.getFrequency().getCycleInHours());
        routineValues.put("repetitions", routine.getFrequency().getRepetitions());
        int rows = database.update("routines", routineValues, "task_id = " + routine.getId(), null);
        if(rows < 1)
            throw new DatabaseException(R.string.db_exception_update);

        sqLiteOpener.close();
    }

    @Override
    public void delete(int id) throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        int rows = database.delete("routines", "task_id = " + id, null);
        if(rows < 1)
            throw new DatabaseException(R.string.db_exception_delete);
        int taskRows = database.delete("tasks", "id = " + id, null);
        if(taskRows < 1)
            throw new DatabaseException(R.string.db_exception_delete);

        sqLiteOpener.close();
    }

    @SuppressLint("Range")
    @Override
    public Routine findOne(int id) throws DatabaseException, ResourceNotFoundException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        Routine routine = null;
        String sql =
            "SELECT t.id, t.description, t.task_type, r.hour, r.minute, r.cycle_in_hours, r.repetitions FROM tasks t " +
            "INNER JOIN routines r ON t.id = r.task_id " +
            "WHERE task_type = 'ROUTINE' AND id = " + id + ";";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            routine = new Routine();
            routine.setFrequency(new Frequency());
            routine.setId(cursor.getInt(cursor.getColumnIndex("id")));
            routine.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            routine.setType(TaskType.valueOf(cursor.getString(cursor.getColumnIndex("task_type"))));
            routine.getFrequency().setHour(cursor.getInt(cursor.getColumnIndex("hour")));
            routine.getFrequency().setMinute(cursor.getInt(cursor.getColumnIndex("minute")));
            routine.getFrequency().setCycleInHours(cursor.getInt(cursor.getColumnIndex("cycle_in_hours")));
            routine.getFrequency().setRepetitions(cursor.getInt(cursor.getColumnIndex("repetitions")));
            cursor.moveToNext();
        }
        cursor.close();
        if(routine == null)
            throw new ResourceNotFoundException();

        sqLiteOpener.close();

        return routine;
    }

    @SuppressLint("Range")
    @Override
    public List<Routine> findAll() throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        List<Routine> routines = new ArrayList<>();
        String sql =
            "SELECT t.id, t.description, t.task_type, r.hour, r.minute, r.cycle_in_hours, r.repetitions FROM tasks t " +
            "INNER JOIN routines r ON t.id = r.task_id " +
            "WHERE task_type = 'ROUTINE';";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Routine routine = new Routine();
            routine.setFrequency(new Frequency());
            routine.setId(cursor.getInt(cursor.getColumnIndex("id")));
            routine.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            routine.setType(TaskType.valueOf(cursor.getString(cursor.getColumnIndex("task_type"))));
            routine.getFrequency().setHour(cursor.getInt(cursor.getColumnIndex("hour")));
            routine.getFrequency().setMinute(cursor.getInt(cursor.getColumnIndex("minute")));
            routine.getFrequency().setCycleInHours(cursor.getInt(cursor.getColumnIndex("cycle_in_hours")));
            routine.getFrequency().setRepetitions(cursor.getInt(cursor.getColumnIndex("repetitions")));
            routines.add(routine);
            cursor.moveToNext();
        }
        cursor.close();

        sqLiteOpener.close();

        return routines;
    }
}
