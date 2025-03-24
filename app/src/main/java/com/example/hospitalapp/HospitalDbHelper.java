package com.example.hospitalapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HospitalDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hospital.db";
    private static final int DATABASE_VERSION = 5;

    public HospitalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPatientsTable = "CREATE TABLE Patients (" +
                "patient_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "first_name TEXT, " +
                "last_name TEXT, " +
                "date_of_birth TEXT, " +
                "phone TEXT, " +
                "email TEXT, " +
                "address TEXT, " +
                "doctor_id INTEGER, " +
                "diagnosis TEXT, " +
                "treatment TEXT, " +
                "medications TEXT, " +
                "ward TEXT, " +
                "FOREIGN KEY(doctor_id) REFERENCES Doctors(doctor_id))";

        String createDoctorsTable = "CREATE TABLE Doctors (" +
                "doctor_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "first_name TEXT, " +
                "last_name TEXT, " +
                "specialization TEXT, " +
                "phone TEXT, " +
                "email TEXT)";

        db.execSQL(createPatientsTable);
        db.execSQL(createDoctorsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Patients");
        db.execSQL("DROP TABLE IF EXISTS Doctors");
        onCreate(db);
    }
}
