package com.example.hospitalapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AddPatientActivity extends AppCompatActivity {
    private HospitalDbHelper dbHelper;
    private EditText firstNameEditText, lastNameEditText, dobEditText, phoneEditText, emailEditText, addressEditText,
            diagnosisEditText, treatmentEditText, medicationsEditText, wardEditText;
    private Spinner doctorSpinner;
    private List<Doctor> doctorList;
    private ArrayAdapter<Doctor> doctorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        dbHelper = new HospitalDbHelper(this);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        dobEditText = findViewById(R.id.dobEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        addressEditText = findViewById(R.id.addressEditText);
        diagnosisEditText = findViewById(R.id.diagnosisEditText);
        treatmentEditText = findViewById(R.id.treatmentEditText);
        medicationsEditText = findViewById(R.id.medicationsEditText);
        wardEditText = findViewById(R.id.wardEditText);
        doctorSpinner = findViewById(R.id.doctorSpinner);

        doctorList = new ArrayList<>();
        doctorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, doctorList);
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorSpinner.setAdapter(doctorAdapter);

        loadDoctors();

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> savePatient());
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

        if (doctorList.isEmpty()) {
            Toast.makeText(this, "Список врачей пуст. Добавьте врача перед добавлением пациента.", Toast.LENGTH_LONG).show();
        }

        doctorAdapter.notifyDataSetChanged();
    }

    private void savePatient() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String dob = dobEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String diagnosis = diagnosisEditText.getText().toString();
        String treatment = treatmentEditText.getText().toString();
        String medications = medicationsEditText.getText().toString();
        String ward = wardEditText.getText().toString();
        Doctor selectedDoctor = (Doctor) doctorSpinner.getSelectedItem();
        int doctorId = selectedDoctor != null ? selectedDoctor.getId() : -1;

        if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || doctorId == -1) {
            Toast.makeText(this, "Заполните обязательные поля", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("date_of_birth", dob);
        values.put("phone", phone);
        values.put("email", email);
        values.put("address", address);
        values.put("doctor_id", doctorId);
        values.put("diagnosis", diagnosis);
        values.put("treatment", treatment);
        values.put("medications", medications);
        values.put("ward", ward);

        long newRowId = db.insert("Patients", null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Пациент добавлен", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка добавления пациента", Toast.LENGTH_SHORT).show();
        }
    }
}
