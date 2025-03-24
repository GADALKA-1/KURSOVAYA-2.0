package com.example.hospitalapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddPatientActivity extends AppCompatActivity {
    private static final String TAG = "AddPatientActivity";
    private HospitalDbHelper dbHelper;
    private EditText firstNameEditText, lastNameEditText, dobEditText, phoneEditText, emailEditText, addressEditText,
            diagnosisEditText, treatmentEditText, medicationsEditText, wardEditText, admissionDateEditText;
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
        admissionDateEditText = findViewById(R.id.admissionDateEditText);
        doctorSpinner = findViewById(R.id.doctorSpinner);

        // Устанавливаем текущую дату по умолчанию
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        admissionDateEditText.setText(currentDate);

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
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String dob = dobEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String diagnosis = diagnosisEditText.getText().toString().trim();
        String treatment = treatmentEditText.getText().toString().trim();
        String medications = medicationsEditText.getText().toString().trim();
        String ward = wardEditText.getText().toString().trim();
        String admissionDate = admissionDateEditText.getText().toString().trim();
        Doctor selectedDoctor = (Doctor) doctorSpinner.getSelectedItem();
        int doctorId = selectedDoctor != null ? selectedDoctor.getId() : -1;

        // Проверка обязательных полей
        if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || admissionDate.isEmpty()) {
            Toast.makeText(this, "Заполните обязательные поля: имя, фамилия, дата рождения, дата поступления", Toast.LENGTH_SHORT).show();
            return;
        }

        if (doctorList.isEmpty() || doctorId == -1) {
            Toast.makeText(this, "Выберите врача из списка", Toast.LENGTH_SHORT).show();
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
        values.put("admission_date", admissionDate);
        values.put("admission_count", 1); // Первое поступление

        try {
            long newRowId = db.insert("Patients", null, values);
            if (newRowId != -1) {
                // Сохраняем историю изменений
                ContentValues historyValues = new ContentValues();
                historyValues.put("patient_id", newRowId);
                historyValues.put("diagnosis", diagnosis);
                historyValues.put("treatment", treatment);
                historyValues.put("medications", medications);
                historyValues.put("ward", ward);
                historyValues.put("update_date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                db.insert("PatientHistory", null, historyValues);

                Toast.makeText(this, "Пациент добавлен", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Log.e(TAG, "Ошибка добавления пациента: insert вернул -1");
                Toast.makeText(this, "Ошибка добавления пациента", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Исключение при добавлении пациента: " + e.getMessage(), e);
            Toast.makeText(this, "Ошибка добавления пациента: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }
}
