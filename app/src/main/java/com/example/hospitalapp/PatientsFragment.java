package com.example.hospitalapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PatientsFragment extends Fragment {
    private HospitalDbHelper dbHelper;
    private List<Patient> patientList;
    private PatientAdapter patientAdapter;
    private boolean isListVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patients, container, false);

        dbHelper = new HospitalDbHelper(getContext());
        patientList = new ArrayList<>();
        patientAdapter = new PatientAdapter(getContext(), patientList);

        Button addButton = view.findViewById(R.id.addButton);
        Button listPatientsButton = view.findViewById(R.id.listPatientsButton);
        ListView listView = view.findViewById(R.id.listView);
        TextView emptyTextView = view.findViewById(R.id.emptyTextView);
        listView.setEmptyView(emptyTextView);

        listView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.GONE);

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddPatientActivity.class);
            startActivity(intent);
        });

        listPatientsButton.setOnClickListener(v -> {
            if (isListVisible) {
                listView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.GONE);
                listPatientsButton.setText("Список пациентов");
                isListVisible = false;
            } else {
                listView.setAdapter(patientAdapter);
                loadPatients();
                listView.setVisibility(View.VISIBLE);
                listPatientsButton.setText("Скрыть список");
                isListVisible = true;
            }
        });

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            if (position < patientList.size()) {
                Patient patient = patientList.get(position);
                showPatientDetailsDialog(patient);
            }
        });

        return view;
    }

    private void loadPatients() {
        patientList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Patients", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            String middleName = cursor.getString(cursor.getColumnIndexOrThrow("middle_name"));
            String dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow("date_of_birth"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            int doctorId = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
            String diagnosis = cursor.getString(cursor.getColumnIndexOrThrow("diagnosis"));
            String treatment = cursor.getString(cursor.getColumnIndexOrThrow("treatment"));
            String medications = cursor.getString(cursor.getColumnIndexOrThrow("medications"));
            String ward = cursor.getString(cursor.getColumnIndexOrThrow("ward"));
            String admissionDate = cursor.getString(cursor.getColumnIndexOrThrow("admission_date"));
            int admissionCount = cursor.getInt(cursor.getColumnIndexOrThrow("admission_count"));
            patientList.add(new Patient(id, firstName, lastName, middleName, dateOfBirth, phone, email, address, doctorId, diagnosis, treatment, medications, ward, admissionDate, admissionCount));
        }
        cursor.close();
        patientAdapter.notifyDataSetChanged();
    }

    private void showPatientDetailsDialog(Patient patient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Информация о пациенте");

        String details = "Имя: " + patient.getFirstName() + "\n" +
                "Фамилия: " + patient.getLastName() + "\n" +
                "Отчество: " + patient.getMiddleName() + "\n" +
                "Дата рождения: " + patient.getDateOfBirth() + "\n" +
                "Телефон: " + patient.getPhone() + "\n" +
                "Email: " + patient.getEmail() + "\n" +
                "Адрес: " + patient.getAddress() + "\n" +
                "Диагноз: " + patient.getDiagnosis() + "\n" +
                "Лечение: " + patient.getTreatment() + "\n" +
                "Лекарства: " + patient.getMedications() + "\n" +
                "Палата: " + patient.getWard() + "\n" +
                "Количество поступлений: " + patient.getAdmissionCount(); // Добавляем строку с количеством поступлений
        builder.setMessage(details);

        builder.setNeutralButton("Выписать", (dialog, which) -> {
            dischargePatient(patient);
        });

        builder.setPositiveButton("Закрыть", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void dischargePatient(Patient patient) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues archiveValues = new ContentValues();
            archiveValues.put("patient_id", patient.getId());
            archiveValues.put("first_name", patient.getFirstName());
            archiveValues.put("last_name", patient.getLastName());
            archiveValues.put("middle_name", patient.getMiddleName());
            archiveValues.put("date_of_birth", patient.getDateOfBirth());
            archiveValues.put("phone", patient.getPhone());
            archiveValues.put("email", patient.getEmail());
            archiveValues.put("address", patient.getAddress());
            archiveValues.put("doctor_id", patient.getDoctorId());
            archiveValues.put("diagnosis", patient.getDiagnosis());
            archiveValues.put("treatment", patient.getTreatment());
            archiveValues.put("medications", patient.getMedications());
            archiveValues.put("ward", patient.getWard());
            archiveValues.put("admission_date", patient.getAdmissionDate());
            archiveValues.put("discharge_date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
            archiveValues.put("admission_count", patient.getAdmissionCount());

            long archiveRowId = db.insert("Archive", null, archiveValues);
            if (archiveRowId != -1) {
                int deletedRows = db.delete("Patients", "patient_id = ?", new String[]{String.valueOf(patient.getId())});
                if (deletedRows > 0) {
                    Toast.makeText(getContext(), "Пациент выписан", Toast.LENGTH_SHORT).show();
                    loadPatients();
                } else {
                    Toast.makeText(getContext(), "Ошибка при удалении пациента", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Ошибка при добавлении в архив", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isListVisible) {
            loadPatients();
        }
    }
}
