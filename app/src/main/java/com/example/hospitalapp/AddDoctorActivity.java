package com.example.hospitalapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddDoctorActivity extends AppCompatActivity {
    private HospitalDbHelper dbHelper;
    private EditText firstNameEditText, lastNameEditText, specializationEditText, phoneEditText, emailEditText;

    // Список специальностей
    private final String[] specializations = {
            "Терапевт", "Невролог", "Гинеколог", "Кардиолог", "Хирург",
            "Онколог", "Педиатр", "Стоматолог", "Фармацевт"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        dbHelper = new HospitalDbHelper(this);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        specializationEditText = findViewById(R.id.specializationEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);

        // Делаем поле "Специальность" некликабельным напрямую, чтобы пользователь не мог вводить текст
        specializationEditText.setKeyListener(null);

        // Показываем диалог при нажатии на поле "Специальность"
        specializationEditText.setOnClickListener(v -> showSpecializationDialog());

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveDoctor());
    }

    private void showSpecializationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите специальность");

        builder.setItems(specializations, (dialog, which) -> {
            // Устанавливаем выбранную специальность в EditText
            specializationEditText.setText(specializations[which]);
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void saveDoctor() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String specialization = specializationEditText.getText().toString().trim();


        if (firstName.isEmpty() || lastName.isEmpty() || specialization.isEmpty()) {
            Toast.makeText(this, "Заполните обязательные поля: имя, фамилия, специальность", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("specialization", specialization);


        long newRowId = db.insert("Doctors", null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Врач добавлен", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка добавления врача", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}
