package com.edu.fateczl.youmorehealthy.persistence.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SQLiteOpener extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "YOUMOREHEALTHY.DB";
    private static final int DATABASE_VERSION = 1;

    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", new Locale("pt", "BR"));

    private static final String TASKS_TABLE =
            "CREATE TABLE tasks(" +
                "id INTEGER PRIMARY KEY," +
                "description VARCHAR(30) NOT NULL," +
                "task_type VARCHAR(20) NOT NULL," +
                "UNIQUE(description, task_type)" +
            ");";

    private static final String EVENTS_TABLE =
            "CREATE TABLE events(" +
                "task_id INTEGER, " +
                "event_date CHAR(16) NOT NULL, " +
                "PRIMARY KEY(task_id)," +
                "FOREIGN KEY(task_id) REFERENCES tasks(id)" +
            ");";

    private static final String DOCTORS_TABLE =
            "CREATE TABLE doctors(" +
                "id INTEGER PRIMARY KEY," +
                "name VARCHAR(30) NOT NULL," +
                "speciality VARCHAR(30) NOT NULL," +
                "UNIQUE(name, speciality)" +
            ");";

    private static final String MEDIC_APPOINTMENTS_TABLE =
            "CREATE TABLE medic_appointments(" +
                "event_task_id INTEGER," +
                "diagnostic VARCHAR(100)," +
                "doctor_id INTEGER," +
                "PRIMARY KEY(event_task_id)," +
                "FOREIGN KEY(event_task_id) REFERENCES events(task_id)," +
                "FOREIGN KEY(doctor_id) REFERENCES doctors(id)" +
            ");";

    private static final String ROUTINES_TABLE =
            "CREATE TABLE routines(" +
                "task_id INTEGER," +
                "hour INTEGER NOT NULL," +
                "minute INTEGER NOT NULL," +
                "cycle_in_hours INTEGER NOT NULL," +
                "repetitions INTEGER NOT NULL," +
                "PRIMARY KEY(task_id)," +
                "FOREIGN KEY(task_id) REFERENCES tasks(id)" +
            ");";

    private static final String TRAININGS_TABLE =
            "CREATE TABLE trainings(" +
                "id INTEGER PRIMARY KEY," +
                "result VARCHAR(100)," +
                "training_date CHAR(16) NOT NULL," +
                "routine_task_id INTEGER," +
                "FOREIGN KEY(routine_task_id) REFERENCES routines(task_id)" +
            ");";

    private static final String MEDICINES_TABLE =
            "CREATE TABLE medicines(" +
                "id INTEGER PRIMARY KEY," +
                "name VARCHAR(30) NOT NULL," +
                "dose VARCHAR(30) NOT NULL," +
                "UNIQUE(name, dose)" +
            ");";

    private static final String MEDICINE_CONSUMES =
            "CREATE TABLE medicine_consumes(" +
                "routine_task_id INTEGER," +
                "medicine_id INTEGER," +
                "PRIMARY KEY(routine_task_id)," +
                "FOREIGN KEY(routine_task_id) REFERENCES routines(task_id)," +
                "FOREIGN KEY(medicine_id) REFERENCES medicines(id)" +
            ");";

    public SQLiteOpener(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TASKS_TABLE);
        db.execSQL(EVENTS_TABLE);
        db.execSQL(DOCTORS_TABLE);
        db.execSQL(MEDIC_APPOINTMENTS_TABLE);
        db.execSQL(ROUTINES_TABLE);
        db.execSQL(TRAININGS_TABLE);
        db.execSQL(MEDICINES_TABLE);
        db.execSQL(MEDICINE_CONSUMES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS medic_appointments");
            db.execSQL("DROP TABLE IF EXISTS doctors");
            db.execSQL("DROP TABLE IF EXISTS events");
            db.execSQL("DROP TABLE IF EXISTS trainings");
            db.execSQL("DROP TABLE IF EXISTS medicines");
            db.execSQL("DROP TABLE IF EXISTS medicine_consumes");
            db.execSQL("DROP TABLE IF EXISTS routines");
            db.execSQL("DROP TABLE IF EXISTS tasks");
            this.onCreate(db);
        }
    }
}
