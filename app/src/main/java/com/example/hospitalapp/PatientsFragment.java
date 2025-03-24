package com.example.hospitalapp;

import android.app.AlertDialog;
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
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class PatientsFragment extends Fragment {
    private HospitalDbHelper dbHelper;
    private List<Patient> patientList;
    private PatientAdapter adapter;
    private boolean isListVisible = false; // Переменная для отслеживания состояния списка

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patients, container, false);

        dbHelper = new HospitalDbHelper(getContext());
        patientList = new ArrayList<>();
        adapter = new PatientAdapter(getContext(), patientList);

        ListView listView = view.findViewById(R.id.listView);
        TextView emptyTextView = view.findViewById(R.id.emptyTextView);
        listView.setEmptyView(emptyTextView);
        listView.setAdapter(adapter);

        Button addButton = view.findViewById(R.id.addButton);
        Button listPatientsButton = view.findViewById(R.id.listPatientsButton);

        // Изначально скрываем список
        listView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.GONE);

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddPatientActivity.class);
            startActivity(intent);
        });

        listPatientsButton.setOnClickListener(v -> {
            if (isListVisible) {
                // Если список виден, скрываем его
                listView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.GONE);
                listPatientsButton.setText("Список пациентов");
                isListVisible = false;
            } else {
                // Если список скрыт, показываем его
                loadPatients();
                listView.setVisibility(View.VISIBLE);
                listPatientsButton.setText("Скрыть список");
                isListVisible = true;
            }
        });

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            if (position < patientList.size()) {
                Patient patient = patientList.get(position);
                showPatientDetailsDialog(patient, position);
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
            String dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow("date_of_birth"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            int doctorId = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
            String diagnosis = cursor.getString(cursor.getColumnIndexOrThrow("diagnosis"));
            String treatment = cursor.getString(cursor.getColumnIndexOrThrow("treatment"));
            String medications = cursor.getString(cursor.getColumnIndexOrThrow("medications"));
            String ward = cursor.getString(cursor.getColumnIndexOrThrow("ward"));
            patientList.add(new Patient(id, firstName, lastName, dateOfBirth, phone, email, address, doctorId, diagnosis, treatment, medications, ward));
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
                "Палата: " + patient.getWard();
        builder.setMessage(details);

        builder.setPositiveButton("Закрыть", (dialog, which) -> dialog.dismiss());
        builder.setNegativeButton("Выписать", (dialog, which) -> {
            dischargePatient(patient.getId(), position);
        });

        builder.create().show();
    }

    private void dischargePatient(int patientId, int position) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete("Patients", "patient_id = ?", new String[]{String.valueOf(patientId)});
        if (rowsDeleted > 0) {
            patientList.remove(position);
            adapter.notifyDataSetChanged();
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
