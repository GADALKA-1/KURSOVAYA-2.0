package com.example.hospitalapp;

import android.content.ContentValues;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EditPatientActivity extends AppCompatActivity {
// "public" — модификатор доступа, класс доступен из любого места
// "class" — ключевое слово для объявления класса
// "EditPatientActivity" — имя класса (активность для редактирования пациента)
// "extends" — ключевое слово для наследования
// "AppCompatActivity" — родительский класс, от которого наследуется этот класс
// Этот класс представляет активность для редактирования данных пациента

    // Объявление приватных полей класса
    private static final String TAG = "EditPatientActivity";
    // "private" — модификатор доступа, поле доступно только внутри класса
    // "static" — модификатор, делает поле статическим (принадлежит классу, а не объекту)
    // "final" — модификатор, делает поле неизменяемым (константой)
    // "String" — тип данных, строка
    // "TAG" — имя переменной, используется для логирования
    // "EditPatientActivity" — значение, строка с именем класса
    // Эта строка создаёт константу для логирования сообщений

    private HospitalDbHelper dbHelper;
    // "HospitalDbHelper" — тип данных, наш класс для работы с базой данных
    // "dbHelper" — имя переменной, будет хранить объект для взаимодействия с базой
    // Точка с запятой (;) — завершает строку кода

    private EditText firstNameEditText, lastNameEditText, middleNameEditText, dobEditText, phoneEditText, emailEditText, addressEditText,
            diagnosisEditText, treatmentEditText, medicationsEditText, wardEditText, admissionDateEditText, doctorEditText;
    // "EditText" — тип данных, виджет для ввода текста
    // "firstNameEditText" и другие — имена переменных, каждая хранит ссылку на поле ввода
    // Запятая (,) — разделяет переменные одного типа
    // Эти переменные будут хранить ссылки на поля ввода из макета

    private List<Doctor> doctorList;
    // "List" — тип данных, интерфейс для списка
    // "<Doctor>" — обобщение, указывает, что список будет содержать объекты типа Doctor
    // "doctorList" — имя переменной, будет хранить список врачей

    private Map<String, List<String>> specializationDiagnoses;
    // "Map" — тип данных, интерфейс для ассоциативного массива
    // "<String, List<String>>" — обобщение, ключ — строка, значение — список строк
    // "specializationDiagnoses" — имя переменной, будет хранить соответствие специальностей и диагнозов

    private String[] currentDiagnoses = new String[0];
    // "String" — тип данных, строка
    // "[]" — объявление массива
    // "currentDiagnoses" — имя переменной, массив для хранения текущих диагнозов
    // "new" — создаёт новый объект
    // "String[0]" — создаёт пустой массив строк
    // Эта строка создаёт пустой массив для хранения диагнозов

    private Doctor selectedDoctor;
    // "Doctor" — тип данных, наш класс для представления врача
    // "selectedDoctor" — имя переменной, будет хранить выбранного врача

    private Patient patient;
    // "Patient" — тип данных, наш класс для представления пациента
    // "patient" — имя переменной, будет хранить данные пациента для редактирования

    private int originalPatientId;
    // "int" — тип данных, целое число
    // "originalPatientId" — имя переменной, будет хранить оригинальный ID пациента

    private final String[] specializations = {
            "Терапевт", "Невролог", "Гинеколог", "Кардиолог", "Хирург",
            "Онколог", "Педиатр", "Стоматолог", "Фармацевт"
    };
    // "final" — делает массив неизменяемым
    // "String[]" — тип данных, массив строк
    // "specializations" — имя переменной, массив специальностей врачей
    // "{" — начало инициализации массива
    // "Терапевт" и другие — элементы массива, строки
    // "}" — конец инициализации массива
    // Эта строка создаёт массив с названиями специальностей

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // "super" — ключевое слово, обращается к родительскому классу (AppCompatActivity)
        // "onCreate" — вызов метода onCreate родительского класса
        // "savedInstanceState" — передаём параметр в родительский метод
        // Эта строка вызывает onCreate родительского класса для базовой инициализации

        setContentView(R.layout.activity_add_patient);
        // "setContentView" — метод, задаёт макет для активности
        // "R" — класс ресурсов
        // "layout" — подмодуль, содержит макеты
        // "activity_add_patient" — имя XML-файла макета (res/layout/activity_add_patient.xml)
        // Эта строка задаёт макет для активности (используем тот же макет, что для добавления пациента)

        dbHelper = new HospitalDbHelper(this);
        // "dbHelper" — переменная
        // "new" — создаёт новый объект
        // "HospitalDbHelper" — класс, конструктор которого вызываем
        // "this" — текущий объект (активность), передаётся как контекст
        // Эта строка создаёт объект для работы с базой данных

        firstNameEditText = findViewById(R.id.firstNameEditText);
        // "firstNameEditText" — переменная
        // "findViewById" — метод, ищет элемент в макете по ID
        // "R.id.firstNameEditText" — ID элемента в XML (android:id="@+id/firstNameEditText")
        // Эта строка находит поле ввода для имени в макете

        lastNameEditText = findViewById(R.id.lastNameEditText);
        // Аналогично, находит поле для фамилии

        middleNameEditText = findViewById(R.id.middleNameEditText);
        // Находит поле для отчества

        dobEditText = findViewById(R.id.dobEditText);
        // Находит поле для даты рождения

        phoneEditText = findViewById(R.id.phoneEditText);
        // Находит поле для телефона

        emailEditText = findViewById(R.id.emailEditText);
        // Находит поле для email

        addressEditText = findViewById(R.id.addressEditText);
        // Находит поле для адреса

        diagnosisEditText = findViewById(R.id.diagnosisEditText);
        // Находит поле для диагноза

        treatmentEditText = findViewById(R.id.treatmentEditText);
        // Находит поле для метода лечения

        medicationsEditText = findViewById(R.id.medicationsEditText);
        // Находит поле для лекарств

        wardEditText = findViewById(R.id.wardEditText);
        // Находит поле для палаты

        admissionDateEditText = findViewById(R.id.admissionDateEditText);
        // Находит поле для даты поступления

        doctorEditText = findViewById(R.id.doctorEditText);
        // Находит поле для выбора врача

        Intent intent = getIntent();
        // "Intent" — тип переменной
        // "intent" — имя переменной
        // "getIntent" — метод, возвращает Intent, с которым была запущена активность
        // Эта строка получает Intent, содержащий данные пациента
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
        // "patient" — переменная
        // "new" — создаёт новый объект
        // "Patient" — класс, конструктор которого вызываем
        // "intent" — объект Intent
        // "getIntExtra" — метод, извлекает целое число по ключу
        // "patient_id" — ключ
        // "-1" — значение по умолчанию
        // "getStringExtra" — метод, извлекает строку по ключу
        // Эта строка создаёт объект Patient с данными из Intent

        // Получаем оригинальный patient_id из таблицы Archive
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // "SQLiteDatabase" — тип переменной
        // "db" — имя переменной
        // "dbHelper" — объект HospitalDbHelper
        // "getReadableDatabase" — метод, открывает базу для чтения
        // Эта строка открывает базу данных для чтения

        Cursor cursor = db.query("Archive", new String[]{"patient_id"}, "archive_id = ?", new String[]{String.valueOf(patient.getId())}, null, null, null);
        // "Cursor" — тип переменной
        // "cursor" — имя переменной
        // "db" — объект базы данных
        // "query" — метод, выполняет SQL-запрос
        // "Archive" — имя таблицы
        // "new String[]{"patient_id"}" — массив столбцов для выборки
        // "archive_id = ?" — условие WHERE
        // "new String[]{String.valueOf(patient.getId())}" — значения для заполнителя
        // "patient" — объект Patient
        // "getId" — метод, возвращает ID
        // "String.valueOf" — преобразует ID в строку
        // "null" — остальные параметры (группировка, сортировка и т.д.)
        // Эта строка выполняет запрос для получения patient_id из таблицы Archive

        if (cursor.moveToFirst()) {
            // "if" — условный оператор
            // "cursor" — объект Cursor
            // "moveToFirst" — метод, переходит к первой строке результата
            // Условие проверяет, есть ли данные

            originalPatientId = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));
            // "originalPatientId" — переменная
            // "getInt" — метод, извлекает целое число
            // "getColumnIndexOrThrow" — метод, возвращает индекс столбца
            // "patient_id" — имя столбца
            // Эта строка извлекает patient_id из результата запроса
        } else {
            // "else" — выполняется, если условие if ложно

            originalPatientId = -1;
            // Присваиваем -1, если patient_id не найден
        }
        // "}" — конец блока if-else

        cursor.close();
        // "close" — метод, закрывает курсор
        // Эта строка закрывает курсор

        db.close();
        // "close" — метод, закрывает базу данных
        // Эта строка закрывает базу данных

        firstNameEditText.setText(patient.getFirstName());
        // "firstNameEditText" — поле ввода
        // "setText" — метод, задаёт текст в поле
        // "patient" — объект Patient
        // "getFirstName" — метод, возвращает имя
        // Эта строка заполняет поле имени данными пациента

        lastNameEditText.setText(patient.getLastName());
        // Аналогично, заполняет поле фамилии

        middleNameEditText.setText(patient.getMiddleName());
        // Заполняет поле отчества

        dobEditText.setText(patient.getDateOfBirth());
        // Заполняет поле даты рождения

        phoneEditText.setText(patient.getPhone());
        // Заполняет поле телефона

        emailEditText.setText(patient.getEmail());
        // Заполняет поле email

        addressEditText.setText(patient.getAddress());
        // Заполняет поле адреса

        diagnosisEditText.setText(patient.getDiagnosis());
        // Заполняет поле диагноза

        treatmentEditText.setText(patient.getTreatment());
        // Заполняет поле метода лечения

        medicationsEditText.setText(patient.getMedications());
        // Заполняет поле лекарств

        wardEditText.setText(patient.getWard());
        // Заполняет поле палаты

        admissionDateEditText.setText(patient.getAdmissionDate());
        // Заполняет поле даты поступления

        doctorEditText.setKeyListener(null);
        // "doctorEditText" — поле для выбора врача
        // "setKeyListener" — метод, задаёт слушатель клавиатуры
        // "null" — отключает ввод с клавиатуры
        // Эта строка делает поле врача некликабельным для ввода текста

        diagnosisEditText.setKeyListener(null);
        // Аналогично, отключает ввод для поля диагноза

        doctorEditText.setOnTouchListener((v, event) -> {
            // "setOnTouchListener" — метод, задаёт обработчик касаний
            // "v" — параметр, View, на котором произошло касание
            // "event" — параметр, объект MotionEvent (событие касания)
            // "->" — лямбда-выражение
            // Эта строка задаёт обработчик касаний для поля врача

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // "event" — объект MotionEvent
                // "getAction" — метод, возвращает тип действия
                // "MotionEvent.ACTION_DOWN" — константа, обозначает нажатие
                // "==" — оператор сравнения
                // Условие проверяет, что произошло нажатие

                showSpecializationDialog();
                // "showSpecializationDialog" — метод, определённый ниже
                // Эта строка вызывает метод для отображения диалога выбора специальности

                return true;
                // "return" — возвращает значение
                // "true" — означает, что событие обработано
                // Эта строка указывает, что касание обработано
            }
            // "}" — конец блока if

            return false;
            // "false" — означает, что событие не обработано
            // Эта строка возвращает false, если условие не выполнено
        });
        // "}" — конец лямбда-выражения
        // ")" — конец параметров метода
        // ";" — конец строки

        diagnosisEditText.setOnTouchListener((v, event) -> {
            // Аналогично, задаёт обработчик касаний для поля диагноза

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Проверяет нажатие

                showDiagnosisDialog();
                // "showDiagnosisDialog" — метод, определённый ниже
                // Эта строка вызывает метод для отображения диалога выбора диагноза

                return true;
                // Указывает, что событие обработано
            }

            return false;
            // Возвращает false, если событие не обработано
        });

        doctorList = new ArrayList<>();
        // "doctorList" — переменная
        // "new" — создаёт новый объект
        // "ArrayList" — класс, реализующий список
        // "<>" — обобщение, указывает тип элементов
        // Эта строка создаёт пустой список для врачей

        initializeSpecializationDiagnoses();
        // "initializeSpecializationDiagnoses" — метод, определённый ниже
        // Эта строка вызывает метод для инициализации соответствия специальностей и диагнозов

        loadDoctors();
        // "loadDoctors" — метод, определённый ниже
        // Эта строка вызывает метод для загрузки врачей из базы данных

        for (Doctor doctor : doctorList) {
            // "for" — цикл
            // "Doctor" — тип переменной
            // "doctor" — имя переменной, каждый элемент списка
            // ":" — разделитель в цикле for-each
            // "doctorList" — список, по которому проходим
            // Цикл проходит по всем врачам в списке

            if (doctor.getId() == patient.getDoctorId()) {
                // "doctor" — текущий врач
                // "getId" — метод, возвращает ID врача
                // "patient" — объект Patient
                // "getDoctorId" — метод, возвращает ID врача пациента
                // "==" — сравнение
                // Условие проверяет, совпадает ли ID врача с ID врача пациента

                selectedDoctor = doctor;
                // "selectedDoctor" — переменная
                // "doctor" — текущий врач
                // Эта строка сохраняет выбранного врача

                doctorEditText.setText(doctor.getLastName() + " " + doctor.getFirstName() + " " + doctor.getMiddleName());
                // "setText" — задаёт текст в поле
                // "getLastName" — возвращает фамилию
                // "+" — конкатенация строк
                // " " — пробел
                // Эта строка заполняет поле врача ФИО

                updateDiagnosisList(doctor.getSpecialization());
                // "updateDiagnosisList" — метод, определённый ниже
                // "getSpecialization" — возвращает специальность врача
                // Эта строка обновляет список диагнозов для специальности врача

                break;
                // "break" — прерывает цикл
                // Эта строка прерывает цикл, так как врач найден
            }
            // "}" — конец блока if
        }
        // "}" — конец цикла for

        Button saveButton = findViewById(R.id.saveButton);
        // "Button" — тип переменной
        // "saveButton" — имя переменной
        // "R.id.saveButton" — ID кнопки в XML
        // Эта строка находит кнопку "Сохранить"

        saveButton.setText("Сохранить изменения");
        // "setText" — задаёт текст кнопки
        // "Сохранить изменения" — новый текст
        // Эта строка меняет текст кнопки

        saveButton.setOnClickListener(v -> savePatient());
        // "setOnClickListener" — метод, задаёт обработчик кликов
        // "v" — параметр, View, на котором произошёл клик
        // "->" — лямбда-выражение
        // "savePatient" — метод, определённый ниже
        // Эта строка задаёт действие при клике на кнопку
    }

    private void initializeSpecializationDiagnoses() {
        // "private" — модификатор доступа
        // "void" — метод ничего не возвращает
        // "initializeSpecializationDiagnoses" — имя метода
        // Этот метод заполняет Map соответствиями

        specializationDiagnoses = new HashMap<>();
        // "specializationDiagnoses" — переменная
        // "new" — создаёт новый объект
        // "HashMap" — класс, реализующий Map
        // "<>" — обобщение
        // Эта строка создаёт пустой HashMap

        specializationDiagnoses.put("Терапевт", Arrays.asList("ОРВИ и грипп", "Бронхит", "Гастрит", "Гипертония", "Анемия"));
        // "put" — метод, добавляет пару ключ-значение
        // "Терапевт" — ключ, строка
        // "Arrays" — класс для работы с массивами
        // "asList" — метод, преобразует массив в список
        // "ОРВИ и грипп" и другие — элементы списка
        // Эта строка добавляет диагнозы для терапевта

        specializationDiagnoses.put("Невролог", Arrays.asList("Остеохондроз", "Межпозвоночная грыжа", "Вегетососудистая дистония (ВСД)", "Невралгия", "Мигрень"));
        // Аналогично, добавляет диагнозы для невролога

        specializationDiagnoses.put("Гинеколог", Arrays.asList("Эрозия шейки матки", "Кандидоз (молочница)", "Миома матки", "Поликистоз яичников", "Эндометриоз"));
        // Добавляет диагнозы для гинеколога

        specializationDiagnoses.put("Кардиолог", Arrays.asList("Гипертоническая болезнь", "Ишемическая болезнь сердца (ИБС)", "Аритмия", "Сердечная недостаточность", "Перикардит"));
        // Добавляет диагнозы для кардиолога

        specializationDiagnoses.put("Хирург", Arrays.asList("Грыжа (паховая, пупочная)", "Варикозное расширение вен", "Аппендицит", "Гнойные абсцессы", "Переломы пальца(ев)", "Переломы запястья", "Перелом лучевой кости", "Перелом ключицы", "Перелом нижней челюсти", "Перелом левого бедра", "Перелом правого бедра", "Перелом левых костей голени", "Перелом правых костей голени"));
        // Добавляет диагнозы для хирурга

        specializationDiagnoses.put("Онколог", Arrays.asList("Рак молочной железы", "Рак легких", "Лейкоз (рак крови)", "Меланома", "Лимфома"));
        // Добавляет диагнозы для онколога

        specializationDiagnoses.put("Педиатр", Arrays.asList("Диатез", "Коклюш", "Корь", "Рахит", "Детские инфекции (ветрянка, краснуха)"));
        // Добавляет диагнозы для педиатра

        specializationDiagnoses.put("Стоматолог", Arrays.asList("Кариес", "Пульпит", "Пародонтоз", "Гингивит", "Флюс (периостит)"));
        // Добавляет диагнозы для стоматолога

        specializationDiagnoses.put("Фармацевт", Arrays.asList("Головная боль", "Аллергия", "Боль в суставах", "Желудочно-кишечные расстройства"));
        // Добавляет диагнозы для фармацевта
    }
    // "}" — конец метода initializeSpecializationDiagnoses

    // Метод для обновления списка диагнозов на основе специальности
    private void updateDiagnosisList(String specialization) {
        // "String" — тип параметра
        // "specialization" — имя параметра
        // Этот метод обновляет массив currentDiagnoses

        List<String> diagnoses = specializationDiagnoses.get(specialization);
        // "List<String>" — тип переменной
        // "diagnoses" — имя переменной
        // "specializationDiagnoses" — Map
        // "get" — метод, возвращает значение по ключу
        // "specialization" — ключ
        // Эта строка получает список диагнозов для специальности

        if (diagnoses != null) {
            // "if" — условный оператор
            // "diagnoses" — переменная
            // "!=" — оператор "не равно"
            // "null" — отсутствие значения
            // Условие проверяет, что список диагнозов существует

            currentDiagnoses = diagnoses.toArray(new String[0]);
            // "currentDiagnoses" — массив
            // "diagnoses" — список
            // "toArray" — метод, преобразует список в массив
            // "new String[0]" — создаёт пустой массив для результата
            // Эта строка преобразует список диагнозов в массив
        } else {
            // "else" — выполняется, если условие ложно

            currentDiagnoses = new String[0];
            // Присваиваем пустой массив, если диагнозы не найдены
        }
        // "}" — конец блока if-else
    }

    private void showSpecializationDialog() {
        // Этот метод показывает диалог со списком специальностей

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // "AlertDialog.Builder" — тип переменной
        // "builder" — имя переменной
        // "new" — создаёт новый объект
        // "this" — текущая активность, передаётся как контекст
        // Эта строка создаёт объект для построения диалога

        builder.setTitle("Выберите специальность врача");
        // "setTitle" — метод, задаёт заголовок диалога
        // "Выберите специальность врача" — текст заголовка
        // Эта строка задаёт заголовок диалога

        builder.setItems(specializations, (dialog, which) -> {
            // "setItems" — метод, задаёт список элементов для выбора
            // "specializations" — массив специальностей
            // "dialog" — параметр, объект диалога
            // "which" — параметр, индекс выбранного элемента
            // "->" — лямбда-выражение
            // Эта строка задаёт список специальностей и обработчик выбора

            String selectedSpecialization = specializations[which];
            // "String" — тип переменной
            // "selectedSpecialization" — имя переменной
            // "specializations" — массив
            // "which" — индекс
            // "[]" — доступ к элементу массива
            // Эта строка получает выбранную специальность

            showDoctorsDialog(selectedSpecialization);
            // "showDoctorsDialog" — метод, определённый ниже
            // "selectedSpecialization" — параметр
            // Эта строка вызывает метод для отображения диалога выбора врача
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
        // "setNegativeButton" — метод, задаёт кнопку "Отмена"
        // "Отмена" — текст кнопки
        // "dialog" — параметр
        // "which" — параметр
        // "dismiss" — метод, закрывает диалог
        // Эта строка добавляет кнопку "Отмена"

        builder.create().show();
        // "create" — метод, создаёт диалог
        // "show" — метод, отображает диалог
        // Эта строка создаёт и показывает диалог
    }

    private void showDoctorsDialog(String specialization) {
        // "String" — тип параметра
        // "specialization" — имя параметра
        // Этот метод показывает диалог с врачами заданной специальности

        List<Doctor> doctorsBySpecialization = new ArrayList<>();
        // "List<Doctor>" — тип переменной
        // "doctorsBySpecialization" — имя переменной
        // "new" — создаёт новый объект
        // "ArrayList" — класс
        // Эта строка создаёт список для врачей с заданной специальностью

        for (Doctor doctor : doctorList) {
            // Цикл проходит по всем врачам

            if (doctor.getSpecialization().equals(specialization)) {
                // "getSpecialization" — возвращает специальность врача
                // "equals" — метод, сравнивает строки
                // "specialization" — заданная специальность
                // Условие проверяет, совпадает ли специальность врача

                doctorsBySpecialization.add(doctor);
                // "add" — метод, добавляет врача в список
                // "doctor" — текущий врач
                // Эта строка добавляет врача в список
            }
            // "}" — конец блока if
        }

        if (doctorsBySpecialization.isEmpty()) {
            // "isEmpty" — метод, проверяет, пуст ли список
            // Условие проверяет, есть ли врачи

            Toast.makeText(this, "Нет врачей с этой специальностью", Toast.LENGTH_SHORT).show();
            // "Toast" — класс для уведомлений
            // "makeText" — метод, создаёт уведомление
            // "this" — контекст
            // "Нет врачей с этой специальностью" — текст сообщения
            // "Toast.LENGTH_SHORT" — длительность показа (короткая)
            // "show" — метод, показывает уведомление
            // Эта строка показывает уведомление, если врачей нет

            return;
            // "return" — прерывает выполнение метода
            // Эта строка завершает метод
        }

        String[] doctorNames = new String[doctorsBySpecialization.size()];
        // "String[]" — тип переменной, массив строк
        // "doctorNames" — имя переменной
        // "new" — создаёт новый объект
        // "doctorsBySpecialization" — список врачей
        // "size" — метод, возвращает размер списка
        // Эта строка создаёт массив для имён врачей

        for (int i = 0; i < doctorsBySpecialization.size(); i++) {
            // "for" — цикл
            // "int" — тип переменной
            // "i" — счётчик
            // "0" — начальное значение
            // "i < doctorsBySpecialization.size()" — условие
            // "i++" — инкремент
            // Цикл проходит по всем врачам

            Doctor doctor = doctorsBySpecialization.get(i);
            // "Doctor" — тип переменной
            // "doctor" — имя переменной
            // "get" — метод, возвращает элемент по индексу
            // "i" — индекс
            // Эта строка получает врача по индексу

            doctorNames[i] = doctor.getLastName() + " " + doctor.getFirstName() + " " + doctor.getMiddleName();
            // "doctorNames" — массив
            // "i" — индекс
            // "getLastName" и другие — методы, возвращают ФИО
            // "+" — конкатенация
            // Эта строка формирует строку с ФИО врача
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Создаём объект для построения диалога

        builder.setTitle("Выберите врача (" + specialization + ")");

        builder.setItems(doctorNames, (dialog, which) -> {
            // "setItems" — задаёт список имён врачей
            // "doctorNames" — массив имён
            // "dialog" — параметр
            // "which" — индекс выбранного врача

            selectedDoctor = doctorsBySpecialization.get(which);
            // "selectedDoctor" — переменная
            // "get" — метод, возвращает врача по индексу
            // "which" — индекс
            // Эта строка сохраняет выбранного врача

            doctorEditText.setText(selectedDoctor.getLastName() + " " + selectedDoctor.getFirstName() + " " + selectedDoctor.getMiddleName());
            // Заполняет поле врача ФИО

            updateDiagnosisList(selectedDoctor.getSpecialization());
            // Обновляет список диагнозов

            diagnosisEditText.setText("");
            // "setText" — задаёт текст
            // "" — пустая строка
            // Эта строка очищает поле диагноза
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
        // Добавляет кнопку "Отмена"

        builder.create().show();
        // Создаёт и показывает диалог
    }

    private void showDiagnosisDialog() {
        // Этот метод показывает диалог с диагнозами

        if (selectedDoctor == null) {
            // "selectedDoctor" — переменная
            // "==" — сравнение
            // "null" — отсутствие значения
            // Условие проверяет, выбран ли врач

            Toast.makeText(this, "Сначала выберите врача", Toast.LENGTH_SHORT).show();
            // Показывает уведомление

            return;
            // Завершает метод
        }

        if (currentDiagnoses.length == 0) {
            // "currentDiagnoses" — массив
            // "length" — свойство, возвращает длину массива
            // "== 0" — проверяет, пуст ли массив
            // Условие проверяет, есть ли диагнозы

            Toast.makeText(this, "Нет доступных диагнозов для этой специальности", Toast.LENGTH_SHORT).show();
            // Показывает уведомление

            return;
            // Завершает метод
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Создаём объект для построения диалога

        builder.setTitle("Выберите диагноз");
        // Задаём заголовок диалога

        builder.setItems(currentDiagnoses, (dialog, which) -> {
            // "setItems" — задаёт список диагнозов
            // "currentDiagnoses" — массив диагнозов
            // "dialog" — параметр
            // "which" — индекс выбранного диагноза

            diagnosisEditText.setText(currentDiagnoses[which]);
            // Заполняет поле диагноза выбранным значением
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
        // Добавляет кнопку "Отмена"

        builder.create().show();
        // Создаёт и показывает диалог
    }

    private void loadDoctors() {
        // Этот метод загружает врачей из таблицы Doctors

        doctorList.clear();
        // "clear" — метод, очищает список
        // Эта строка очищает список врачей

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Открывает базу данных для чтения

        Cursor cursor = db.query("Doctors", null, null, null, null, null, null);
        // "query" — выполняет запрос к таблице Doctors
        // "Doctors" — имя таблицы
        // "null" — все параметры (выбираем все столбцы, без условий и сортировки)
        // Эта строка выполняет запрос для получения всех врачей

        while (cursor.moveToNext()) {
            // "while" — цикл
            // "moveToNext" — переходит к следующей строке
            // Цикл проходит по всем строкам результата

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
            // "id" — переменная
            // "getInt" — извлекает ID врача

            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
            // Извлекает имя врача

            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            // Извлекает фамилию врача

            String middleName = cursor.getString(cursor.getColumnIndexOrThrow("middle_name"));
            // Извлекает отчество врача

            String specialization = cursor.getString(cursor.getColumnIndexOrThrow("specialization"));
            // Извлекает специальность врача

            doctorList.add(new Doctor(id, firstName, lastName, middleName, specialization));
            // "add" — добавляет врача в список
            // "new Doctor" — создаёт объект Doctor
            // Эта строка добавляет врача в список
        }
        // "}" — конец цикла while

        cursor.close();
        // Закрывает курсор

        if (doctorList.isEmpty()) {
            // Проверяет, пуст ли список врачей

            Toast.makeText(this, "Список врачей пуст. Добавьте врача перед редактированием пациента.", Toast.LENGTH_LONG).show();
            // "Toast.LENGTH_LONG" — длительность показа (длинная)
            // Показывает уведомление
        }
        // "}" — конец блока if
    }

    private void savePatient() {
        // Этот метод сохраняет обновлённые данные пациента

        String firstName = firstNameEditText.getText().toString().trim();
        // "String" — тип переменной
        // "firstName" — имя переменной
        // "firstNameEditText" — поле ввода
        // "getText" — метод, возвращает текст
        // "toString" — преобразует в строку
        // "trim" — метод, удаляет пробелы в начале и конце
        // Эта строка получает имя из поля ввода

        String lastName = lastNameEditText.getText().toString().trim();
        // Аналогично, получает фамилию

        String middleName = middleNameEditText.getText().toString().trim();
        // Получает отчество

        String dob = dobEditText.getText().toString().trim();
        // Получает дату рождения

        String phone = phoneEditText.getText().toString().trim();
        // Получает телефон

        String email = emailEditText.getText().toString().trim();
        // Получает email

        String address = addressEditText.getText().toString().trim();
        // Получает адрес

        String diagnosis = diagnosisEditText.getText().toString().trim();
        // Получает диагноз

        String treatment = treatmentEditText.getText().toString().trim();
        // Получает метод лечения

        String medications = medicationsEditText.getText().toString().trim();
        // Получает лекарства

        String ward = wardEditText.getText().toString().trim();
        // Получает палату

        String admissionDate = admissionDateEditText.getText().toString().trim();
        // Получает дату поступления

        int doctorId = selectedDoctor != null ? selectedDoctor.getId() : -1;
        // "int" — тип переменной
        // "doctorId" — имя переменной
        // "selectedDoctor" — переменная
        // "!=" — сравнение
        // "null" — отсутствие значения
        // "?" — тернарный оператор
        // "selectedDoctor.getId()" — возвращает ID врача, если врач выбран
        // ":" — разделитель в тернарном операторе
        // "-1" — значение по умолчанию
        // Эта строка получает ID врача или -1, если врач не выбран

        if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || admissionDate.isEmpty()) {
            // "isEmpty" — метод, проверяет, пуста ли строка
            // "||" — логическое ИЛИ
            // Условие проверяет, заполнены ли обязательные поля

            Toast.makeText(this, "Заполните обязательные поля: имя, фамилия, дата рождения, дата поступления", Toast.LENGTH_SHORT).show();
            // Показывает уведомление

            return;
            // Завершает метод
        }

        if (selectedDoctor == null || doctorId == -1) {
            // Проверяет, выбран ли врач

            Toast.makeText(this, "Выберите врача", Toast.LENGTH_SHORT).show();
            // Показывает уведомление

            return;
            // Завершает метод
        }

        if (diagnosis.isEmpty()) {
            // Проверяет, выбран ли диагноз

            Toast.makeText(this, "Выберите диагноз", Toast.LENGTH_SHORT).show();
            // Показывает уведомление

            return;
            // Завершает метод
        }

        if (!isValidDateFormat(admissionDate)) {
            // "!" — логическое НЕ
            // "isValidDateFormat" — метод, определённый ниже
            // "admissionDate" — дата поступления
            // Условие проверяет, корректен ли формат даты

            Toast.makeText(this, "Некорректный формат даты поступления (ожидается YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
            // Показывает уведомление

            return;
            // Завершает метод
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // "getWritableDatabase" — метод, открывает базу для записи
        // Эта строка открывает базу данных для записи

        ContentValues values = new ContentValues();
        // "ContentValues" — тип переменной
        // "values" — имя переменной
        // "new" — создаёт новый объект
        // Эта строка создаёт объект для хранения данных

        values.put("patient_id", originalPatientId);
        // "put" — метод, добавляет пару ключ-значение
        // "patient_id" — ключ
        // "originalPatientId" — значение
        // Эта строка добавляет ID пациента

        values.put("first_name", firstName);
        // Добавляет имя

        values.put("last_name", lastName);
        // Добавляет фамилию

        values.put("middle_name", middleName);
        // Добавляет отчество

        values.put("date_of_birth", dob);
        // Добавляет дату рождения

        values.put("phone", phone);
        // Добавляет телефон

        values.put("email", email);
        // Добавляет email

        values.put("address", address);
        // Добавляет адрес

        values.put("doctor_id", doctorId);
        // Добавляет ID врача

        values.put("diagnosis", diagnosis);
        // Добавляет диагноз

        values.put("treatment", treatment);
        // Добавляет метод лечения

        values.put("medications", medications);
        // Добавляет лекарства

        values.put("ward", ward);
        // Добавляет палату

        values.put("admission_date", admissionDate);
        // Добавляет дату поступления

        values.put("admission_count", patient.getAdmissionCount() + 1);
        // "getAdmissionCount" — возвращает текущее количество поступлений
        // "+ 1" — увеличивает на 1
        // Эта строка добавляет увеличенное количество поступлений

        try {
            // "try" — блок для обработки исключений
            // Эта строка начинает блок обработки ошибок

            long newRowId = db.insert("Patients", null, values);
            // "long" — тип переменной
            // "newRowId" — имя переменной
            // "db" — объект базы данных
            // "insert" — метод, вставляет данные в таблицу
            // "Patients" — имя таблицы
            // "null" — параметр, используется, если строка пустая
            // "values" — данные для вставки
            // Эта строка вставляет данные в таблицу Patients

            if (newRowId != -1) {
                // "newRowId" — ID новой строки
                // "!=" — сравнение
                // "-1" — значение, означающее ошибку
                // Условие проверяет, успешно ли вставлена строка

                ContentValues historyValues = new ContentValues();
                // Создаём объект для записи в историю

                historyValues.put("patient_id", originalPatientId);
                // Добавляет ID пациента

                historyValues.put("diagnosis", diagnosis);
                // Добавляет диагноз

                historyValues.put("treatment", treatment);
                // Добавляет метод лечения

                historyValues.put("medications", medications);
                // Добавляет лекарства

                historyValues.put("ward", ward);
                // Добавляет палату

                historyValues.put("update_date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                // "new SimpleDateFormat" — создаёт объект для форматирования даты
                // "yyyy-MM-dd" — формат даты
                // "Locale.getDefault" — метод, возвращает текущую локаль
                // "format" — метод, форматирует дату
                // "new Date()" — создаёт объект с текущей датой
                // Эта строка добавляет текущую дату в формате YYYY-MM-DD

                db.insert("PatientHistory", null, historyValues);
                // "PatientHistory" — имя таблицы
                // Вставляет запись в таблицу истории

                int deletedRows = db.delete("Archive", "archive_id = ?", new String[]{String.valueOf(patient.getId())});
                // "int" — тип переменной
                // "deletedRows" — имя переменной
                // "delete" — метод, удаляет строки из таблицы
                // "Archive" — имя таблицы
                // "archive_id = ?" — условие
                // "new String[]{String.valueOf(patient.getId())}" — значение для заполнителя
                // Эта строка удаляет пациента из таблицы Archive

                if (deletedRows > 0) {
                    // "deletedRows" — количество удалённых строк
                    // ">" — оператор "больше"
                    // "0" — проверяет, были ли удалены строки
                    // Условие проверяет, успешно ли удаление

                    Toast.makeText(this, "Пациент восстановлен", Toast.LENGTH_SHORT).show();
                    // Показывает уведомление об успехе

                    setResult(RESULT_OK);
                    // "setResult" — метод, задаёт результат активности
                    // "RESULT_OK" — константа, означает успешное выполнение
                    // Эта строка задаёт результат для вызывающей активности

                    finish();
                    // "finish" — метод, закрывает активность
                    // Эта строка закрывает активность
                } else {
                    // "else" — выполняется, если удаление не удалось

                    Toast.makeText(this, "Ошибка при удалении из архива", Toast.LENGTH_SHORT).show();
                    // Показывает уведомление об ошибке
                }
                // "}" — конец блока if-else
            } else {
                // "else" — выполняется, если вставка не удалась

                Log.e(TAG, "Ошибка восстановления пациента: insert вернул -1");
                // "Log" — класс для логирования
                // "e" — метод, логирует ошибку
                // "TAG" — тег для лога
                // "Ошибка восстановления пациента: insert вернул -1" — сообщение
                // Эта строка записывает ошибку в лог

                Toast.makeText(this, "Ошибка восстановления пациента", Toast.LENGTH_SHORT).show();
                // Показывает уведомление
            }
            // "}" — конец блока if-else
        } catch (Exception e) {
            // "catch" — блок для обработки исключений
            // "Exception" — тип исключения
            // "e" — имя переменной, объект исключения
            // Эта строка начинает блок обработки ошибок

            Log.e(TAG, "Исключение при восстановлении пациента: " + e.getMessage(), e);
            // "getMessage" — метод, возвращает сообщение об ошибке
            // "e" — объект исключения, передаётся для полного стека вызовов
            // Эта строка записывает исключение в лог

            Toast.makeText(this, "Ошибка восстановления пациента: " + e.getMessage(), Toast.LENGTH_LONG).show();
            // Показывает уведомление с текстом ошибки
        } finally {
            // "finally" — блок, выполняется всегда
            // Эта строка начинает блок, который выполняется в любом случае

            db.close();
            // Закрывает базу данных
        }
        // "}" — конец блока try-catch-finally
    }
    // "}" — конец метода savePatient

    // Метод для проверки формата даты
    private boolean isValidDateFormat(String date) {
        // "boolean" — тип возвращаемого значения
        // "isValidDateFormat" — имя метода
        // "String" — тип параметра
        // "date" — имя параметра
        // Этот метод проверяет, соответствует ли дата формату YYYY-MM-DD

        try {
            // Начинает блок обработки исключений

            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date);
            // "new SimpleDateFormat" — создаёт объект для парсинга даты
            // "parse" — метод, парсит строку в дату
            // "date" — строка для парсинга
            // Эта строка пытается распарсить дату

            return true;
            // Возвращает true, если парсинг успешен
        } catch (Exception e) {
            // Ловит исключение, если парсинг не удался

            return false;
            // Возвращает false, если формат некорректен
        }
        // "}" — конец блока try-catch
    }
    // "}" — конец метода isValidDateFormat
}
