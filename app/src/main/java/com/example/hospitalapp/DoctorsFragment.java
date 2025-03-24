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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class DoctorsFragment extends Fragment {
    private HospitalDbHelper dbHelper;
    private List<Doctor> doctorList;
    private List<Patient> patientList;
    private DoctorAdapter doctorAdapter;
    private PatientAdapter patientAdapter;
    private boolean isListVisible = false; // Переменная для отслеживания состояния списка

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctors, container, false);

        dbHelper = new HospitalDbHelper(getContext());
        doctorList = new ArrayList<>();
        patientList = new ArrayList<>();
        doctorAdapter = new DoctorAdapter(getContext(), doctorList);
        patientAdapter = new PatientAdapter(getContext(), patientList);

        Button addButton = view.findViewById(R.id.addButton);
        Button listDoctorsButton = view.findViewById(R.id.listDoctorsButton);
        Button attachedPatientsButton = view.findViewById(R.id.attachedPatientsButton);
        ListView listView = view.findViewById(R.id.listView);
        TextView emptyTextView = view.findViewById(R.id.emptyTextView);
        listView.setEmptyView(emptyTextView);

        // Изначально скрываем список
        listView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.GONE);

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddDoctorActivity.class);
            startActivity(intent);
        });

        listDoctorsButton.setOnClickListener(v -> {
            if (isListVisible) {
                // Если список виден, скрываем его
                listView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.GONE);
                listDoctorsButton.setText("Список врачей");
                isListVisible = false;
            } else {
                // Если список скрыт, показываем его
                listView.setAdapter(doctorAdapter);
                loadDoctors();
                listView.setVisibility(View.VISIBLE);
                listDoctorsButton.setText("Скрыть список");
                isListVisible = true;
            }
        });

        attachedPatientsButton.setOnClickListener(v -> showAttachedPatientsDialog());

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            if (listView.getAdapter() == patientAdapter && position < patientList.size()) {
                Patient patient = patientList.get(position);
                showPatientDetailsDialog(patient);
            }
        });

        return view;
    }

    private void loadDoctors() {
        doctorList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Doctors", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            String specialization = cursor.getString(cursor.getColumnIndexOrThrow("specialization"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            doctorList.add(new Doctor(id, firstName, lastName, specialization, phone, email));
        }
        cursor.close();
        doctorAdapter.notifyDataSetChanged();
    }

    private void showAttachedPatientsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_attached_patients, null);
        builder.setView(dialogView);

        Spinner doctorSpinner = dialogView.findViewById(R.id.doctorSpinner);
        ListView patientsListView = dialogView.findViewById(R.id.patientsListView);
        TextView emptyPatientsTextView = dialogView.findViewById(R.id.emptyPatientsTextView);
        patientsListView.setEmptyView(emptyPatientsTextView);

        ArrayAdapter<Doctor> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, doctorList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorSpinner.setAdapter(spinnerAdapter);
        loadDoctors();

        if (doctorList.isEmpty()) {
            emptyPatientsTextView.setVisibility(View.VISIBLE);
            emptyPatientsTextView.setText("Нет врачей для выбора");
            patientsListView.setVisibility(View.GONE);
        } else {
            patientsListView.setAdapter(patientAdapter);
            patientsListView.setVisibility(View.VISIBLE);
            emptyPatientsTextView.setVisibility(View.GONE);

            doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Doctor selectedDoctor = doctorList.get(position);
                    loadPatientsForDoctor(selectedDoctor.getId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    patientList.clear();
                    patientAdapter.notifyDataSetChanged();
                }
            });

            patientsListView.setOnItemClickListener((parent, view, position, id) -> {
                if (position < patientList.size()) {
                    Patient patient = patientList.get(position);
                    showPatientDetailsDialog(patient);
                }
            });
        }

        builder.setPositiveButton("Закрыть", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void loadPatientsForDoctor(int doctorId) {
        patientList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Patients", null, "doctor_id = ?", new String[]{String.valueOf(doctorId)}, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            String dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow("date_of_birth"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            int docId = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
            String diagnosis = cursor.getString(cursor.getColumnIndexOrThrow("diagnosis"));
            String treatment = cursor.getString(cursor.getColumnIndexOrThrow("treatment"));
            String medications = cursor.getString(cursor.getColumnIndexOrThrow("medications"));
            String ward = cursor.getString(cursor.getColumnIndexOrThrow("ward"));
            patientList.add(new Patient(id, firstName, lastName, dateOfBirth, phone, email, address, docId, diagnosis, treatment, medications, ward));
        }
        cursor.close();
        patientAdapter.notifyDataSetChanged();
    }

    private void showPatientDetailsDialog(Patient patient) {
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
        builder.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isListVisible) {
            loadDoctors();
        }
    }
}
