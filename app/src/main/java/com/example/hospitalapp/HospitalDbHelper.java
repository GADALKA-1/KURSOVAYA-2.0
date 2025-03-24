package com.example.hospitalapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HospitalDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hospital.db";
    private static final int DATABASE_VERSION = 11; // Увеличиваем версию базы данных

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
                "admission_date TEXT, " + // Новое поле для даты поступления
                "admission_count INTEGER DEFAULT 1, " + // Новое поле для количества поступлений
                "FOREIGN KEY(doctor_id) REFERENCES Doctors(doctor_id))";

        String createDoctorsTable = "CREATE TABLE Doctors (" +
                "doctor_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "first_name TEXT, " +
                "last_name TEXT, " +
                "specialization TEXT, " +
                "phone TEXT, " +
                "email TEXT)";

        String createArchiveTable = "CREATE TABLE Archive (" +
                "archive_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "patient_id INTEGER, " +
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
                "admission_date TEXT, " +
                "discharge_date TEXT, " + // Дата выписки
                "admission_count INTEGER)";

        String createPatientHistoryTable = "CREATE TABLE PatientHistory (" +
                "history_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "patient_id INTEGER, " +
                "diagnosis TEXT, " +
                "treatment TEXT, " +
                "medications TEXT, " +
                "ward TEXT, " +
                "update_date TEXT, " + // Дата обновления
                "FOREIGN KEY(patient_id) REFERENCES Patients(patient_id))";

        db.execSQL(createPatientsTable);
        db.execSQL(createDoctorsTable);
        db.execSQL(createArchiveTable);
        db.execSQL(createPatientHistoryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Добавляем новые поля в таблицу Patients
            db.execSQL("ALTER TABLE Patients ADD COLUMN admission_date TEXT");
            db.execSQL("ALTER TABLE Patients ADD COLUMN admission_count INTEGER DEFAULT 1");

            // Создаём новые таблицы
            String createArchiveTable = "CREATE TABLE Archive (" +
                    "archive_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "patient_id INTEGER, " +
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
                    "admission_date TEXT, " +
                    "discharge_date TEXT, " +
                    "admission_count INTEGER)";
            db.execSQL(createArchiveTable);

            String createPatientHistoryTable = "CREATE TABLE PatientHistory (" +
                    "history_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "patient_id INTEGER, " +
                    "diagnosis TEXT, " +
                    "treatment TEXT, " +
                    "medications TEXT, " +
                    "ward TEXT, " +
                    "update_date TEXT, " +
                    "FOREIGN KEY(patient_id) REFERENCES Patients(patient_id))";
            db.execSQL(createPatientHistoryTable);
        }
    }
}
