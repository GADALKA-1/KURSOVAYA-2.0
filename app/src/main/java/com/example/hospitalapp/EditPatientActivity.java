package com.example.hospitalapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EditPatientActivity extends AppCompatActivity {
    private static final String TAG = "EditPatientActivity";
    private static final String PREFS_NAME = "AppPrefs";
    private static final String THEME_KEY = "AppTheme";
    private static final int THEME_MORNING_FROST = 0;
    private static final int THEME_MIDNIGHT_FOREST = 1;
    private static final int THEME_GOLDEN_HOUR = 2;

    private HospitalDbHelper dbHelper;
    private EditText firstNameEditText, lastNameEditText, middleNameEditText, dobEditText, phoneEditText, emailEditText, addressEditText,
            diagnosisEditText, treatmentEditText, medicationsEditText, wardEditText, admissionDateEditText, doctorEditText;
    private List<Doctor> doctorList;
    private Map<String, List<String>> specializationDiagnoses;
    private String[] currentDiagnoses = new String[0];
    private Doctor selectedDoctor;
    private Patient patient;
    private int originalPatientId;
    private final String[] specializations = {
            "Терапевт", "Невролог", "Гинеколог", "Кардиолог", "Хирург",
            "Онколог", "Педиатр", "Стоматолог", "Фармацевт"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Применяем тему перед вызовом super.onCreate()
        applyTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        dbHelper = new HospitalDbHelper(this);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        middleNameEditText = findViewById(R.id.middleNameEditText);
        dobEditText = findViewById(R.id.dobEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        addressEditText = findViewById(R.id.addressEditText);
        diagnosisEditText = findViewById(R.id.diagnosisEditText);
        treatmentEditText = findViewById(R.id.treatmentEditText);
        medicationsEditText = findViewById(R.id.medicationsEditText);
        wardEditText = findViewById(R.id.wardEditText);
        admissionDateEditText = findViewById(R.id.admissionDateEditText);
        doctorEditText = findViewById(R.id.doctorEditText);

        Intent intent = getIntent();
        patient = new Patient(
                intent.getIntExtra("patient_id", -1),
                intent.getStringExtra("first_name"),
                intent.getStringExtra("last_name"),
                intent.getStringExtra("middle_name"),
                intent.getStringExtra("date_of_birth"),
                intent.getStringExtra("phone"),
                intent.getStringExtra("email"),
                intent.getStringExtra("address"),
                intent.getIntExtra("doctor_id", -1),
                intent.getStringExtra("diagnosis"),
                intent.getStringExtra("treatment"),
                intent.getStringExtra("medications"),
                intent.getStringExtra("ward"),
                intent.getStringExtra("admission_date"),
                intent.getIntExtra("admission_count", 1)
        );

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Archive", new String[]{"patient_id"}, "archive_id = ?", new String[]{String.valueOf(patient.getId())}, null, null, null);
        if (cursor.moveToFirst()) {
            originalPatientId = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));
        } else {
            originalPatientId = -1;
        }
        cursor.close();
        db.close();

        firstNameEditText.setText(patient.getFirstName());
        lastNameEditText.setText(patient.getLastName());
        middleNameEditText.setText(patient.getMiddleName());
        dobEditText.setText(patient.getDateOfBirth());
        phoneEditText.setText(patient.getPhone());
        emailEditText.setText(patient.getEmail());
        addressEditText.setText(patient.getAddress());
        diagnosisEditText.setText(patient.getDiagnosis());
        treatmentEditText.setText(patient.getTreatment());
        medicationsEditText.setText(patient.getMedications());
        wardEditText.setText(patient.getWard());
        admissionDateEditText.setText(patient.getAdmissionDate());

        doctorEditText.setKeyListener(null);
        diagnosisEditText.setKeyListener(null);

        doctorEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                showSpecializationDialog();
                return true;
            }
            return false;
        });

        diagnosisEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                showDiagnosisDialog();
                return true;
            }
            return false;
        });

        doctorList = new ArrayList<>();
        initializeSpecializationDiagnoses();
        loadDoctors();

        for (Doctor doctor : doctorList) {
            if (doctor.getId() == patient.getDoctorId()) {
                selectedDoctor = doctor;
                doctorEditText.setText(doctor.getLastName() + " " + doctor.getFirstName() + " " + doctor.getMiddleName());
                updateDiagnosisList(doctor.getSpecialization());
                break;
            }
        }

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setText("Сохранить изменения");
        saveButton.setOnClickListener(v -> savePatient());
    }

    private void applyTheme() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int theme = prefs.getInt(THEME_KEY, THEME_MORNING_FROST);
        switch (theme) {
            case THEME_MORNING_FROST:
                setTheme(R.style.Theme_MorningFrost);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_MIDNIGHT_FOREST:
                setTheme(R.style.Theme_MidnightForest);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case THEME_GOLDEN_HOUR:
                setTheme(R.style.Theme_GoldenHour);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
    }

    private void initializeSpecializationDiagnoses() {
        specializationDiagnoses = new HashMap<>();
        specializationDiagnoses.put("Терапевт", Arrays.asList("ОРВИ и грипп", "Бронхит", "Гастрит", "Гипертония", "Анемия"));
        specializationDiagnoses.put("Невролог", Arrays.asList("Остеохондроз", "Межпозвоночная грыжа", "Вегетососудистая дистония (ВСД)", "Невралгия", "Мигрень"));
        specializationDiagnoses.put("Гинеколог", Arrays.asList("Эрозия шейки матки", "Кандидоз (молочница)", "Миома матки", "Поликистоз яичников", "Эндометриоз"));
        specializationDiagnoses.put("Кардиолог", Arrays.asList("Гипертоническая болезнь", "Ишемическая болезнь сердца (ИБС)", "Аритмия", "Сердечная недостаточность", "Перикардит"));
        specializationDiagnoses.put("Хирург", Arrays.asList("Грыжа (паховая, пупочная)", "Варикозное расширение вен", "Аппендицит", "Гнойные абсцессы", "Переломы пальца(ев)", "Переломы запястья", "Перелом лучевой кости", "Перелом ключицы", "Перелом нижней челюсти", "Перелом левого бедра", "Перелом правого бедра", "Перелом левых костей голени", "Перелом правых костей голени"));
        specializationDiagnoses.put("Онколог", Arrays.asList("Рак молочной железы", "Рак легких", "Лейкоз (рак крови)", "Меланома", "Лимфома"));
        specializationDiagnoses.put("Педиатр", Arrays.asList("Диатез", "Коклюш", "Корь", "Рахит", "Детские инфекции (ветрянка, краснуха)"));
        specializationDiagnoses.put("Стоматолог", Arrays.asList("Кариес", "Пульпит", "Пародонтоз", "Гингивит", "Флюс (периостит)"));
        specializationDiagnoses.put("Фармацевт", Arrays.asList("Головная боль", "Аллергия", "Боль в суставах", "Желудочно-кишечные расстройства"));
    }

    private void updateDiagnosisList(String specialization) {
        List<String> diagnoses = specializationDiagnoses.get(specialization);
        if (diagnoses != null) {
            currentDiagnoses = diagnoses.toArray(new String[0]);
        } else {
            currentDiagnoses = new String[0];
        }
    }

    private void showSpecializationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите специальность врача");

        builder.setItems(specializations, (dialog, which) -> {
            String selectedSpecialization = specializations[which];
            showDoctorsDialog(selectedSpecialization);
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void showDoctorsDialog(String specialization) {
        List<Doctor> doctorsBySpecialization = new ArrayList<>();
        for (Doctor doctor : doctorList) {
            if (doctor.getSpecialization().equals(specialization)) {
                doctorsBySpecialization.add(doctor);
            }
        }

        if (doctorsBySpecialization.isEmpty()) {
            Toast.makeText(this, "Нет врачей с этой специальностью", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] doctorNames = new String[doctorsBySpecialization.size()];
        for (int i = 0; i < doctorsBySpecialization.size(); i++) {
            Doctor doctor = doctorsBySpecialization.get(i);
            doctorNames[i] = doctor.getLastName() + " " + doctor.getFirstName() + " " + doctor.getMiddleName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите врача (" + specialization + ")");

        builder.setItems(doctorNames, (dialog, which) -> {
            selectedDoctor = doctorsBySpecialization.get(which);
            doctorEditText.setText(selectedDoctor.getLastName() + " " + selectedDoctor.getFirstName() + " " + selectedDoctor.getMiddleName());
            updateDiagnosisList(selectedDoctor.getSpecialization());
            diagnosisEditText.setText("");
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void showDiagnosisDialog() {
        if (selectedDoctor == null) {
            Toast.makeText(this, "Сначала выберите врача", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentDiagnoses.length == 0) {
            Toast.makeText(this, "Нет доступных диагнозов для этой специальности", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите диагноз");

        builder.setItems(currentDiagnoses, (dialog, which) -> {
            diagnosisEditText.setText(currentDiagnoses[which]);
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void loadDoctors() {
        doctorList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Doctors", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            String middleName = cursor.getString(cursor.getColumnIndexOrThrow("middle_name"));
            String specialization = cursor.getString(cursor.getColumnIndexOrThrow("specialization"));
            doctorList.add(new Doctor(id, firstName, lastName, middleName, specialization));
        }
        cursor.close();

        if (doctorList.isEmpty()) {
            Toast.makeText(this, "Список врачей пуст. Добавьте врача перед редактированием пациента.", Toast.LENGTH_LONG).show();
        }
    }

    private void savePatient() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String middleName = middleNameEditText.getText().toString().trim();
        String dob = dobEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String diagnosis = diagnosisEditText.getText().toString().trim();
        String treatment = treatmentEditText.getText().toString().trim();
        String medications = medicationsEditText.getText().toString().trim();
        String ward = wardEditText.getText().toString().trim();
        String admissionDate = admissionDateEditText.getText().toString().trim();
        int doctorId = selectedDoctor != null ? selectedDoctor.getId() : -1;

        if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || admissionDate.isEmpty()) {
            Toast.makeText(this, "Заполните обязательные поля: имя, фамилия, дата рождения, дата поступления", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDoctor == null || doctorId == -1) {
            Toast.makeText(this, "Выберите врача", Toast.LENGTH_SHORT).show();
            return;
        }

        if (diagnosis.isEmpty()) {
            Toast.makeText(this, "Выберите диагноз", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidDateFormat(admissionDate)) {
            Toast.makeText(this, "Некорректный формат даты поступления (ожидается YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("patient_id", originalPatientId);
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("middle_name", middleName);
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
        values.put("admission_count", patient.getAdmissionCount() + 1);

        try {
            long newRowId = db.insert("Patients", null, values);
            if (newRowId != -1) {
                ContentValues historyValues = new ContentValues();
                historyValues.put("patient_id", originalPatientId);
                historyValues.put("diagnosis", diagnosis);
                historyValues.put("treatment", treatment);
                historyValues.put("medications", medications);
                historyValues.put("ward", ward);
                historyValues.put("update_date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                db.insert("PatientHistory", null, historyValues);

                int deletedRows = db.delete("Archive", "archive_id = ?", new String[]{String.valueOf(patient.getId())});
                if (deletedRows > 0) {
                    Toast.makeText(this, "Пациент восстановлен", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Ошибка при удалении из архива", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e(TAG, "Ошибка восстановления пациента: insert вернул -1");
                Toast.makeText(this, "Ошибка восстановления пациента", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Исключение при восстановлении пациента: " + e.getMessage(), e);
            Toast.makeText(this, "Ошибка восстановления пациента: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }

    private boolean isValidDateFormat(String date) {
        try {
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}