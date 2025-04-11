package com.example.hospitalapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class PatientAdapter extends ArrayAdapter<Patient> {
    private HospitalDbHelper dbHelper;

    public PatientAdapter(Context context, List<Patient> patients) {
        super(context, 0, patients);
        dbHelper = new HospitalDbHelper(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_patient, parent, false);
        }

        Patient patient = getItem(position);

        // Находим все TextView
        TextView nameTextView = convertView.findViewById(R.id.patientNameTextView);
        TextView dateOfBirthTextView = convertView.findViewById(R.id.dateOfBirthTextView);
        TextView phoneTextView = convertView.findViewById(R.id.phoneTextView);
        TextView emailTextView = convertView.findViewById(R.id.emailTextView);
        TextView addressTextView = convertView.findViewById(R.id.addressTextView);
        TextView doctorTextView = convertView.findViewById(R.id.doctorTextView);
        TextView diagnosisTextView = convertView.findViewById(R.id.diagnosisTextView);
        TextView treatmentTextView = convertView.findViewById(R.id.treatmentTextView);
        TextView medicationsTextView = convertView.findViewById(R.id.medicationsTextView);
        TextView wardTextView = convertView.findViewById(R.id.wardTextView);

        LinearLayout expandableLayout = convertView.findViewById(R.id.expandableLayout);

        // Заполняем основную информацию (ФИО)
        nameTextView.setText(patient.getLastName() + " " + patient.getFirstName() + " " + patient.getMiddleName());

        // Заполняем дополнительную информацию
        dateOfBirthTextView.setText("Дата рождения: " + patient.getDateOfBirth());
        phoneTextView.setText("Телефон: " + patient.getPhone());
        emailTextView.setText("Email: " + patient.getEmail());
        addressTextView.setText("Адрес: " + patient.getAddress());

        // Информация о враче
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
        doctorTextView.setText(doctorInfo);

        // Остальные данные пациента
        diagnosisTextView.setText("Диагноз: " + patient.getDiagnosis());
        treatmentTextView.setText("Лечение: " + patient.getTreatment());
        medicationsTextView.setText("Лекарства: " + patient.getMedications());
        wardTextView.setText("Палата: " + patient.getWard());

        // Обработка клика для разворачивания/сворачивания
        nameTextView.setOnClickListener(v -> {
            if (expandableLayout.getVisibility() == View.VISIBLE) {
                expandableLayout.setVisibility(View.GONE);
            } else {
                expandableLayout.setVisibility(View.VISIBLE);
            }
        });

        return convertView;
    }
}