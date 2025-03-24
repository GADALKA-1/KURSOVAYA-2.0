package com.example.hospitalapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ArchiveFragment extends Fragment {
    private HospitalDbHelper dbHelper;
    private List<Patient> archiveList;
    private PatientAdapter adapter;
    private boolean isListVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archive, container, false);

        dbHelper = new HospitalDbHelper(getContext());
        archiveList = new ArrayList<>();
        adapter = new PatientAdapter(getContext(), archiveList);

        ListView listView = view.findViewById(R.id.listView);
        TextView emptyTextView = view.findViewById(R.id.emptyTextView);
        listView.setEmptyView(emptyTextView);
        listView.setAdapter(adapter);

        Button listArchiveButton = view.findViewById(R.id.listArchiveButton);
        EditText searchDateEditText = view.findViewById(R.id.searchDateEditText);
        Button searchButton = view.findViewById(R.id.searchButton);

        listView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.GONE);

        listArchiveButton.setOnClickListener(v -> {
            if (isListVisible) {
                listView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.GONE);
                listArchiveButton.setText("Список архива");
                isListVisible = false;
            } else {
                loadArchive(null); // Загружаем всех пациентов из архива
                listView.setVisibility(View.VISIBLE);
                listArchiveButton.setText("Скрыть список");
                isListVisible = true;
            }
        });

        searchButton.setOnClickListener(v -> {
            String searchDate = searchDateEditText.getText().toString();
            if (!searchDate.isEmpty()) {
                loadArchive(searchDate);
                listView.setVisibility(View.VISIBLE);
                listArchiveButton.setText("Скрыть список");
                isListVisible = true;
            }
        });

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            if (position < archiveList.size()) {
                Patient patient = archiveList.get(position);
                showPatientDetailsDialog(patient, position);
            }
        });

        return view;
    }

    private void loadArchive(String searchDate) {
        archiveList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = searchDate != null ? "admission_date = ?" : null;
        String[] selectionArgs = searchDate != null ? new String[]{searchDate} : null;
        Cursor cursor = db.query("Archive", null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
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
            archiveList.add(new Patient(id, firstName, lastName, dateOfBirth, phone, email, address, doctorId, diagnosis, treatment, medications, ward, admissionDate, admissionCount));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void showPatientDetailsDialog(Patient patient, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Информация о пациенте");

        String details = "Имя: " + patient.getFirstName() + "\n" +
                "Фамилия: " + patient.getLastName() + "\n" +
                "Дата рождения: " + patient.getDateOfBirth() + "\n" +
                "Телефон: " + patient.getPhone() + "\n" +
                "Email: " + patient.getEmail() + "\n" +
                "Адрес: " + patient.getAddress() + "\n" +
                "Диагноз: " + patient.getDiagnosis() + "\n" +
                "Лечение: " + patient.getTreatment() + "\n" +
                "Лекарства: " + patient.getMedications() + "\n" +
                "Палата: " + patient.getWard() + "\n" +
                "Дата поступления: " + patient.getAdmissionDate() + "\n" +
                "Количество поступлений: " + patient.getAdmissionCount();
        builder.setMessage(details);

        builder.setPositiveButton("Закрыть", (dialog, which) -> dialog.dismiss());
        builder.setNeutralButton("История", (dialog, which) -> showPatientHistory(patient.getId()));
        builder.setNegativeButton("Восстановить", (dialog, which) -> showRestoreDialog(patient, position));

        builder.create().show();
    }

    private void showPatientHistory(int patientId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("PatientHistory", null, "patient_id = ?", new String[]{String.valueOf(patientId)}, null, null, "update_date DESC");
        StringBuilder history = new StringBuilder();
        while (cursor.moveToNext()) {
            String diagnosis = cursor.getString(cursor.getColumnIndexOrThrow("diagnosis"));
            String treatment = cursor.getString(cursor.getColumnIndexOrThrow("treatment"));
            String medications = cursor.getString(cursor.getColumnIndexOrThrow("medications"));
            String ward = cursor.getString(cursor.getColumnIndexOrThrow("ward"));
            String updateDate = cursor.getString(cursor.getColumnIndexOrThrow("update_date"));
            history.append("Дата обновления: ").append(updateDate).append("\n")
                    .append("Диагноз: ").append(diagnosis).append("\n")
                    .append("Лечение: ").append(treatment).append("\n")
                    .append("Лекарства: ").append(medications).append("\n")
                    .append("Палата: ").append(ward).append("\n\n");
        }
        cursor.close();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("История изменений");
        builder.setMessage(history.length() > 0 ? history.toString() : "История пуста");
        builder.setPositiveButton("Закрыть", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void showRestoreDialog(Patient patient, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_restore_patient, null);
        builder.setView(dialogView);

        EditText diagnosisEditText = dialogView.findViewById(R.id.diagnosisEditText);
        EditText treatmentEditText = dialogView.findViewById(R.id.treatmentEditText);
        EditText medicationsEditText = dialogView.findViewById(R.id.medicationsEditText);
        EditText wardEditText = dialogView.findViewById(R.id.wardEditText);

        // Устанавливаем текущие значения
        diagnosisEditText.setText(patient.getDiagnosis());
        treatmentEditText.setText(patient.getTreatment());
        medicationsEditText.setText(patient.getMedications());
        wardEditText.setText(patient.getWard());

        builder.setPositiveButton("Восстановить", (dialog, which) -> {
            String newDiagnosis = diagnosisEditText.getText().toString();
            String newTreatment = treatmentEditText.getText().toString();
            String newMedications = medicationsEditText.getText().toString();
            String newWard = wardEditText.getText().toString();

            restorePatient(patient, position, newDiagnosis, newTreatment, newMedications, newWard);
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void restorePatient(Patient patient, int position, String newDiagnosis, String newTreatment, String newMedications, String newWard) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Перемещаем пациента обратно в таблицу Patients
        ContentValues patientValues = new ContentValues();
        patientValues.put("patient_id", patient.getId());
        patientValues.put("first_name", patient.getFirstName());
        patientValues.put("last_name", patient.getLastName());
        patientValues.put("date_of_birth", patient.getDateOfBirth());
        patientValues.put("phone", patient.getPhone());
        patientValues.put("email", patient.getEmail());
        patientValues.put("address", patient.getAddress());
        patientValues.put("doctor_id", patient.getDoctorId());
        patientValues.put("diagnosis", newDiagnosis);
        patientValues.put("treatment", newTreatment);
        patientValues.put("medications", newMedications);
        patientValues.put("ward", newWard);
        patientValues.put("admission_date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        patientValues.put("admission_count", patient.getAdmissionCount() + 1);

        long patientRowId = db.insert("Patients", null, patientValues);
        if (patientRowId != -1) {
            // Сохраняем историю изменений
            ContentValues historyValues = new ContentValues();
            historyValues.put("patient_id", patient.getId());
            historyValues.put("diagnosis", newDiagnosis);
            historyValues.put("treatment", newTreatment);
            historyValues.put("medications", newMedications);
            historyValues.put("ward", newWard);
            historyValues.put("update_date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
            db.insert("PatientHistory", null, historyValues);

            // Удаляем пациента из архива
            db.delete("Archive", "patient_id = ? AND admission_date = ?", new String[]{String.valueOf(patient.getId()), patient.getAdmissionDate()});
            archiveList.remove(position);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isListVisible) {
            loadArchive(null);
        }
    }
}
