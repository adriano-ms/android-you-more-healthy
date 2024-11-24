package com.edu.fateczl.youmorehealthy.persistence.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.model.Event;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.IEventDao;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class EventDaoSQLite implements IEventDao {

    private final SQLiteOpener sqLiteOpener;
    private SQLiteDatabase database;

    public EventDaoSQLite(SQLiteOpener sqLiteOpener) {
        this.sqLiteOpener = sqLiteOpener;
        database = sqLiteOpener.getWritableDatabase();
    }

    @Override
    public int insert(Event event) throws DatabaseException  {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        ContentValues taskValues = new ContentValues();
        taskValues.put("description", event.getDescription());
        taskValues.put("task_type", event.getType().toString());
        long taskRow = database.insert("tasks",null, taskValues);
        if(taskRow < 0)
            throw new DatabaseException(R.string.db_exception_insert);
        ContentValues eventValues = new ContentValues();
        eventValues.put("task_id", taskRow);
        eventValues.put("event_date", SQLiteOpener.DATE_TIME_FORMAT.format(event.getDate()));
        long row = database.insert("events", null, eventValues);
        if(row < 0)
            throw new DatabaseException(R.string.db_exception_insert);

        sqLiteOpener.close();

        return (int) row;
    }

    @Override
    public void update(Event event) throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        ContentValues taskValues = new ContentValues();
        taskValues.put("description", event.getDescription());
        taskValues.put("task_type", event.getType().toString());
        int taskRows = database.update("tasks", taskValues, "id = " + event.getId(), null);
        if(taskRows < 1)
            throw new DatabaseException(R.string.db_exception_update);
        ContentValues eventValues = new ContentValues();
        eventValues.put("event_date", SQLiteOpener.DATE_TIME_FORMAT.format(event.getDate()));
        int rows = database.update("events", eventValues, "task_id = " + event.getId(), null);
        if(rows < 1)
            throw new DatabaseException(R.string.db_exception_update);

        sqLiteOpener.close();
    }

    @Override
    public void delete(int id) throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        int rows = database.delete("events", "task_id = " + id, null);
        if(rows < 1)
            throw new DatabaseException(R.string.db_exception_delete);
        int taskRows = database.delete("tasks", "id = " + id, null);
        if(taskRows < 1)
            throw new DatabaseException(R.string.db_exception_delete);

        sqLiteOpener.close();
    }

    @SuppressLint("Range")
    @Override
    public Event findOne(int id) throws DatabaseException, ResourceNotFoundException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        Event e = null;
        String sql =
            "SELECT t.id, t.description, t.task_type, e.event_date FROM tasks t " +
            "INNER JOIN events e ON t.id = e.task_id " +
            "WHERE task_type = 'EVENT' AND id = " + id + ";";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            e = new Event();
            e.setId(cursor.getInt(cursor.getColumnIndex("id")));
            e.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            e.setType(TaskType.valueOf(cursor.getString(cursor.getColumnIndex("task_type"))));
            try {
                e.setDate(SQLiteOpener.DATE_TIME_FORMAT.parse(cursor.getString(cursor.getColumnIndex("event_date"))));
            } catch (ParseException ex) {
                throw new DatabaseException(R.string.db_exception_query);
            }
            cursor.moveToNext();
        }
        cursor.close();
        if(e == null)
            throw new ResourceNotFoundException();

        sqLiteOpener.close();

        return e;
    }

    @SuppressLint("Range")
    @Override
    public List<Event> findAll() throws DatabaseException {
        if(!database.isOpen())
            database = sqLiteOpener.getWritableDatabase();

        List<Event> events = new ArrayList<>();
        String sql =
            "SELECT t.id, t.description, t.task_type, e.event_date FROM tasks t " +
            "INNER JOIN events e ON t.id = e.task_id " +
            "WHERE t.task_type = 'EVENT'";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Event e = new Event();
            e.setId(cursor.getInt(cursor.getColumnIndex("id")));
            e.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            e.setType(TaskType.valueOf(cursor.getString(cursor.getColumnIndex("task_type"))));
            try {
                e.setDate(SQLiteOpener.DATE_TIME_FORMAT.parse(cursor.getString(cursor.getColumnIndex("event_date"))));
            } catch (ParseException ex) {
                throw new DatabaseException(R.string.db_exception_query);
            }
            events.add(e);
            cursor.moveToNext();
        }
        cursor.close();

        sqLiteOpener.close();

        return events;
    }
}
