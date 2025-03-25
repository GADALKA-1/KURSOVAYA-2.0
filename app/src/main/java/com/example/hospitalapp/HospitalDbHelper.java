package com.example.hospitalapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HospitalDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hospital.db";
    private static final int DATABASE_VERSION = 6; // Увеличиваем версию базы данных

    public HospitalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPatientsTable = "CREATE TABLE Patients (" +
                "patient_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "first_name TEXT, " +
                "last_name TEXT, " +
                "middle_name TEXT, " + // Новое поле для отчества
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
                "admission_count INTEGER DEFAULT 1, " +
                "FOREIGN KEY(doctor_id) REFERENCES Doctors(doctor_id))";

        String createDoctorsTable = "CREATE TABLE Doctors (" +
                "doctor_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "first_name TEXT, " +
                "last_name TEXT, " +
                "middle_name TEXT, " +
                "specialization TEXT)";

        String createArchiveTable = "CREATE TABLE Archive (" +
                "archive_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "patient_id INTEGER, " +
                "first_name TEXT, " +
                "last_name TEXT, " +
                "middle_name TEXT, " + // Новое поле для отчества
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

        String createPatientHistoryTable = "CREATE TABLE PatientHistory (" +
                "history_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "patient_id INTEGER, " +
                "diagnosis TEXT, " +
                "treatment TEXT, " +
                "medications TEXT, " +
                "ward TEXT, " +
                "update_date TEXT, " +
                "FOREIGN KEY(patient_id) REFERENCES Patients(patient_id))";

        db.execSQL(createPatientsTable);
        db.execSQL(createDoctorsTable);
        db.execSQL(createArchiveTable);
        db.execSQL(createPatientHistoryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE Patients ADD COLUMN admission_date TEXT");
            db.execSQL("ALTER TABLE Patients ADD COLUMN admission_count INTEGER DEFAULT 1");

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

        if (oldVersion < 3) {
            db.execSQL("CREATE TABLE Doctors_temp (" +
                    "doctor_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "first_name TEXT, " +
                    "last_name TEXT, " +
                    "middle_name TEXT, " +
                    "specialization TEXT)");
            db.execSQL("INSERT INTO Doctors_temp (doctor_id, first_name, last_name, specialization) " +
                    "SELECT doctor_id, first_name, last_name, specialization FROM Doctors");
            db.execSQL("DROP TABLE Doctors");
            db.execSQL("ALTER TABLE Doctors_temp RENAME TO Doctors");
        }

        if (oldVersion < 4) {
            // Миграция для таблицы Patients
            db.execSQL("CREATE TABLE Patients_temp (" +
                    "patient_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "first_name TEXT, " +
                    "last_name TEXT, " +
                    "middle_name TEXT, " +
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
                    "admission_count INTEGER DEFAULT 1, " +
                    "FOREIGN KEY(doctor_id) REFERENCES Doctors(doctor_id))");
            db.execSQL("INSERT INTO Patients_temp (patient_id, first_name, last_name, date_of_birth, phone, email, address, doctor_id, diagnosis, treatment, medications, ward, admission_date, admission_count) " +
                    "SELECT patient_id, first_name, last_name, date_of_birth, phone, email, address, doctor_id, diagnosis, treatment, medications, ward, admission_date, admission_count FROM Patients");
            db.execSQL("DROP TABLE Patients");
            db.execSQL("ALTER TABLE Patients_temp RENAME TO Patients");

            // Миграция для таблицы Archive
            db.execSQL("CREATE TABLE Archive_temp (" +
                    "archive_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "patient_id INTEGER, " +
                    "first_name TEXT, " +
                    "last_name TEXT, " +
                    "middle_name TEXT, " +
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
                    "admission_count INTEGER)");
            db.execSQL("INSERT INTO Archive_temp (archive_id, patient_id, first_name, last_name, date_of_birth, phone, email, address, doctor_id, diagnosis, treatment, medications, ward, admission_date, discharge_date, admission_count) " +
                    "SELECT archive_id, patient_id, first_name, last_name, date_of_birth, phone, email, address, doctor_id, diagnosis, treatment, medications, ward, admission_date, discharge_date, admission_count FROM Archive");
            db.execSQL("DROP TABLE Archive");
            db.execSQL("ALTER TABLE Archive_temp RENAME TO Archive");
        }
    }
}
