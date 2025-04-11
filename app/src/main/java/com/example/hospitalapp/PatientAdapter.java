package com.example.hospitalapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PatientAdapter extends ArrayAdapter<Patient> {
    private HospitalDbHelper dbHelper; // Помощник для работы с базой данных

    // Конструктор адаптера
    public PatientAdapter(Context context, List<Patient> patients) {
        super(context, 0, patients);
        dbHelper = new HospitalDbHelper(context); // Инициализация помощника базы данных
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Если convertView пустой, создаём новый view из макета list_item_patient
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_patient, parent, false);
        }

        // Получаем текущего пациента из списка
        Patient patient = getItem(position);

        // Находим текстовые поля в макете
        TextView patientNameTextView = convertView.findViewById(R.id.patientNameTextView);
        TextView doctorInfoTextView = convertView.findViewById(R.id.doctorInfoTextView);
        TextView diagnosisTextView = convertView.findViewById(R.id.diagnosisTextView);

        // Устанавливаем ФИО пациента
        patientNameTextView.setText(patient.getLastName() + " " + patient.getFirstName() + " " + patient.getMiddleName());

        // Извлекаем данные врача из таблицы Doctors
        String doctorInfo = "Врач: Неизвестно";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Doctors", null, "doctor_id = ?", new String[]{String.valueOf(patient.getDoctorId())}, null, null, null);
        if (cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            String middleName = cursor.getString(cursor.getColumnIndexOrThrow("middle_name"));
            String specialization = cursor.getString(cursor.getColumnIndexOrThrow("specialization"));
            doctorInfo = "Врач: " + lastName + " " + firstName + " " + middleName + " (" + specialization + ")";
        }
        cursor.close();
        db.close();

        // Устанавливаем информацию о враче
        doctorInfoTextView.setText(doctorInfo);

        // Устанавливаем диагноз
        diagnosisTextView.setText("Диагноз: " + patient.getDiagnosis());

        return convertView;
    }
}