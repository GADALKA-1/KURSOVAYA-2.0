package com.example.hospitalapp;

import android.app.Activity;
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
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class ArchiveFragment extends Fragment {
    private HospitalDbHelper dbHelper;
    private List<Patient> patientList;
    private PatientAdapter patientAdapter;
    private boolean isListVisible = false;

    private final ActivityResultLauncher<Intent> editPatientLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    loadArchive();
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archive, container, false);

        dbHelper = new HospitalDbHelper(getContext());
        patientList = new ArrayList<>();
        patientAdapter = new PatientAdapter(getContext(), patientList);

        Button listArchiveButton = view.findViewById(R.id.listArchiveButton);
        ListView listView = view.findViewById(R.id.listView);
        TextView emptyTextView = view.findViewById(R.id.emptyTextView);
        listView.setEmptyView(emptyTextView);

        listView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.GONE);

        listArchiveButton.setOnClickListener(v -> {
            if (isListVisible) {
                listView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.GONE);
                listArchiveButton.setText("Список архива");
                isListVisible = false;
            } else {
                listView.setAdapter(patientAdapter);
                loadArchive();
                listView.setVisibility(View.VISIBLE);
                listArchiveButton.setText("Скрыть список");
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

    private void loadArchive() {
        patientList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Archive", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int archiveId = cursor.getInt(cursor.getColumnIndexOrThrow("archive_id"));
            int patientId = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));
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
            // Передаём archiveId как id, но используем patientId для истории
            patientList.add(new Patient(archiveId, firstName, lastName, middleName, dateOfBirth, phone, email, address, doctorId, diagnosis, treatment, medications, ward, admissionDate, admissionCount));
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
                "Количество поступлений: " + patient.getAdmissionCount();
        builder.setMessage(details);

        builder.setPositiveButton("Восстановить", (dialog, which) -> {
            Intent intent = new Intent(getContext(), EditPatientActivity.class);
            intent.putExtra("patient_id", patient.getId());
            intent.putExtra("first_name", patient.getFirstName());
            intent.putExtra("last_name", patient.getLastName());
            intent.putExtra("middle_name", patient.getMiddleName());
            intent.putExtra("date_of_birth", patient.getDateOfBirth());
            intent.putExtra("phone", patient.getPhone());
            intent.putExtra("email", patient.getEmail());
            intent.putExtra("address", patient.getAddress());
            intent.putExtra("doctor_id", patient.getDoctorId());
            intent.putExtra("diagnosis", patient.getDiagnosis());
            intent.putExtra("treatment", patient.getTreatment());
            intent.putExtra("medications", patient.getMedications());
            intent.putExtra("ward", patient.getWard());
            intent.putExtra("admission_date", patient.getAdmissionDate());
            intent.putExtra("admission_count", patient.getAdmissionCount());
            editPatientLauncher.launch(intent);
        });

        builder.setNeutralButton("Просмотр истории", (dialog, which) -> {
            showPatientHistory(patient);
        });

        builder.setNegativeButton("Закрыть", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void showPatientHistory(Patient patient) {
        List<String> historyEntries = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Используем patient_id из таблицы Archive, а не archive_id
        int patientId = -1;
        Cursor cursor = db.query("Archive", new String[]{"patient_id"}, "archive_id = ?", new String[]{String.valueOf(patient.getId())}, null, null, null);
        if (cursor.moveToFirst()) {
            patientId = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));
        }
        cursor.close();

        if (patientId == -1) {
            Toast.makeText(getContext(), "Не удалось найти patient_id", Toast.LENGTH_SHORT).show();
            return;
        }

        cursor = db.query("PatientHistory", null, "patient_id = ?", new String[]{String.valueOf(patientId)}, null, null, "update_date ASC");
        while (cursor.moveToNext()) {
            String diagnosis = cursor.getString(cursor.getColumnIndexOrThrow("diagnosis"));
            String treatment = cursor.getString(cursor.getColumnIndexOrThrow("treatment"));
            String medications = cursor.getString(cursor.getColumnIndexOrThrow("medications"));
            String ward = cursor.getString(cursor.getColumnIndexOrThrow("ward"));
            String updateDate = cursor.getString(cursor.getColumnIndexOrThrow("update_date"));

            String entry = "Дата обновления: " + updateDate + "\n" +
                    "Диагноз: " + diagnosis + "\n" +
                    "Лечение: " + treatment + "\n" +
                    "Лекарства: " + medications + "\n" +
                    "Палата: " + ward + "\n" +
                    "-------------------";
            historyEntries.add(entry);
        }
        cursor.close();
        db.close();

        if (historyEntries.isEmpty()) {
            Toast.makeText(getContext(), "История изменений пуста", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder historyBuilder = new AlertDialog.Builder(getContext());
        historyBuilder.setTitle("История изменений пациента");

        StringBuilder historyText = new StringBuilder();
        for (String entry : historyEntries) {
            historyText.append(entry).append("\n");
        }
        historyBuilder.setMessage(historyText.toString());

        historyBuilder.setPositiveButton("Закрыть", (dialog, which) -> dialog.dismiss());
        historyBuilder.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isListVisible) {
            loadArchive();
        }
    }
}
