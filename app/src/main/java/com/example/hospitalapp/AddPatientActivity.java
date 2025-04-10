package com.example.hospitalapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
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

public class AddPatientActivity extends AppCompatActivity {
// Объявление класса AddPatientActivity, который наследуется от AppCompatActivity.
// "public" — модификатор доступа, делает класс доступным из других классов.
// "class" — ключевое слово для объявления класса.
// "AddPatientActivity" — имя класса, отражающее его назначение (активность для добавления пациента).
// "extends" — ключевое слово, указывающее, что класс наследуется от другого класса.
// "AppCompatActivity" — базовый класс для активностей.

    private static final String TAG = "AddPatientActivity";
    // Объявление константы TAG для логирования.
    // "private" — модификатор доступа, делает переменную доступной только внутри класса.
    // "static" — модификатор, делает переменную статической (принадлежит классу, а не объекту).
    // "final" — модификатор, делает переменную неизменяемой (константой).
    // "String" — тип переменной, строка.
    // "TAG" — имя переменной, используется как тег для логирования.
    // "=" — оператор присваивания.
    // "AddPatientActivity" — строка, значение тега, обычно совпадает с именем класса.
    // ";" — конец инструкции.

    private HospitalDbHelper dbHelper;
    // Объявление переменной dbHelper типа HospitalDbHelper.
    // "private" — модификатор доступа, делает переменную доступной только внутри класса.
    // "HospitalDbHelper" — тип переменной, пользовательский класс для работы с базой данных.
    // "dbHelper" — имя переменной, будет использоваться для доступа к базе данных.
    // ";" — конец инструкции.
    private EditText firstNameEditText, lastNameEditText, middleNameEditText, dobEditText, phoneEditText, emailEditText, addressEditText,
            diagnosisEditText, treatmentEditText, medicationsEditText, wardEditText, admissionDateEditText, doctorEditText;
    // Объявление переменных типа EditText для полей ввода.
    // "private" — модификатор доступа, делает переменные доступными только внутри класса.
    // "EditText" — тип переменных, виджет для ввода текста.
    // "firstNameEditText" — имя переменной, поле для ввода имени пациента.
    // "," — разделитель, позволяет объявить несколько переменных в одной строке.
    // "lastNameEditText" — поле для ввода фамилии.
    // "middleNameEditText" — поле для ввода отчества.
    // "dobEditText" — поле для ввода даты рождения.
    // "phoneEditText" — поле для ввода телефона.
    // "emailEditText" — поле для ввода email.
    // "addressEditText" — поле для ввода адреса.
    // "diagnosisEditText" — поле для ввода диагноза.
    // "treatmentEditText" — поле для ввода лечения.
    // "medicationsEditText" — поле для ввода лекарств.
    // "wardEditText" — поле для ввода палаты.
    // "admissionDateEditText" — поле для ввода даты поступления.
    // "doctorEditText" — поле для отображения выбранного врача.
    // ";" — конец инструкции.

    private List<Doctor> doctorList;
    // Объявление переменной doctorList типа List<Doctor>.
    // "private" — модификатор доступа, делает переменную доступной только внутри класса.
    // "List" — интерфейс, тип коллекции (список).
    // "<Doctor>" — обобщение (generic), указывает, что список будет содержать объекты типа Doctor.
    // "doctorList" — имя переменной, будет хранить список врачей.
    // ";" — конец инструкции.

    private Map<String, List<String>> specializationDiagnoses;
    // Объявление переменной specializationDiagnoses типа Map<String, List<String>>.
    // "private" — модификатор доступа, делает переменную доступной только внутри класса.
    // "Map" — интерфейс, тип ассоциативного массива (ключ-значение).
    // "<String, List<String>>" — обобщение, ключ — строка (специальность), значение — список строк (диагнозы).
    // "specializationDiagnoses" — имя переменной, будет хранить соответствие специальностей и диагнозов.
    // ";" — конец инструкции.
    private String[] currentDiagnoses = new String[0];
    // Объявление переменной currentDiagnoses типа String[].
    // "private" — модификатор доступа, делает переменную доступной только внутри класса.
    // "String[]" — тип переменной, массив строк.
    // "currentDiagnoses" — имя переменной, будет хранить текущий список диагнозов для выбранной специальности.
    // "=" — оператор присваивания.
    // "new" — ключевое слово, создаёт новый объект.
    // "String[0]" — создание пустого массива строк (размер 0).
    // ";" — конец инструкции.

    private Doctor selectedDoctor;
    // Объявление переменной selectedDoctor типа Doctor.
    // "private" — модификатор доступа, делает переменную доступной только внутри класса.
    // "Doctor" — тип переменной, пользовательский класс, представляющий врача.
    // "selectedDoctor" — имя переменной, будет хранить выбранного врача.
    // ";" — конец инструкции.

    private final String[] specializations = {
            "Терапевт", "Невролог", "Гинеколог", "Кардиолог", "Хирург",
            "Онколог", "Педиатр", "Стоматолог", "Фармацевт"
    };
    // Объявление константы specializations типа String[].
    // "private" — модификатор доступа, делает переменную доступной только внутри класса.
    // "final" — модификатор, делает переменную неизменяемой (константой).
    // "String[]" — тип переменной, массив строк.
    // "specializations" — имя переменной, будет хранить список специальностей врачей.
    // "=" — оператор присваивания.
    // "{" — открывающая фигурная скобка, начало массива.
    // "Терапевт", "Невролог", ... — строки, элементы массива (названия специальностей).
    // "," — разделитель элементов массива.
    // "}" — закрывающая фигурная скобка, конец массива.
    // ";" — конец инструкции.

    @Override
    // Аннотация @Override, указывает, что метод переопределяет метод родительского класса.
    // "@" — символ аннотации.
    // "Override" — имя аннотации, используется для проверки переопределения.

    public void onCreate(Bundle savedInstanceState) {
        // Переопределение метода onCreate из класса AppCompatActivity.
        // "public" — модификатор доступа, делает метод доступным из других классов.
        // "void" — тип возвращаемого значения, метод ничего не возвращает.
        // "onCreate" — имя метода, вызывается при создании активности.
        // "Bundle" — тип параметра, объект для хранения данных.
        // "savedInstanceState" — имя параметра, содержит сохранённое состояние активности (например, при повороте экрана).
        // "{" — открывающая фигурная скобка, начало тела метода.

        super.onCreate(savedInstanceState);
        // Вызов метода onCreate родительского класса (AppCompatActivity).
        // "super" — ключевое слово, обращается к родительскому классу.
        // "onCreate" — имя метода родительского класса.
        // "savedInstanceState" — параметр, передаём в родительский метод.
        // ";" — конец инструкции.

        setContentView(R.layout.activity_add_patient);
        // Установка макета для активности.
        // "setContentView" — метод класса AppCompatActivity, задаёт макет интерфейса.
        // "R.layout.activity_add_patient" — идентификатор ресурса макета, где "R" — класс ресурсов, "layout" — тип ресурса, "activity_add_patient" — имя XML-файла.
        // ";" — конец инструкции.

        dbHelper = new HospitalDbHelper(this);
        // Инициализация объекта dbHelper.
        // "dbHelper" — переменная, в которую сохраняем значение.
        // "=" — оператор присваивания.
        // "new" — ключевое слово, создаёт новый объект.
        // "HospitalDbHelper" — класс, объект которого создаём.
        // "this" — ключевое слово, текущий объект активности (контекст).
        // ";" — конец инструкции.

        firstNameEditText = findViewById(R.id.firstNameEditText);
        // Инициализация поля firstNameEditText.
        // "firstNameEditText" — переменная, в которую сохраняем значение.
        // "=" — оператор присваивания.
        // "findViewById" — метод класса AppCompatActivity, находит элемент по идентификатору.
        // "R.id.firstNameEditText" — идентификатор ресурса, где "R" — класс ресурсов, "id" — тип ресурса, "firstNameEditText" — имя идентификатора в XML.
        // ";" — конец инструкции.

        lastNameEditText = findViewById(R.id.lastNameEditText);
        // Инициализация поля lastNameEditText.
        // "lastNameEditText" — переменная.
        // "findViewById" — метод, находит элемент.
        // "R.id.lastNameEditText" — идентификатор ресурса.
        // ";" — конец инструкции.

        middleNameEditText = findViewById(R.id.middleNameEditText);
        // Инициализация поля middleNameEditText.
        // "middleNameEditText" — переменная.
        // "findViewById" — метод.
        // "R.id.middleNameEditText" — идентификатор ресурса.
        // ";" — конец инструкции.

        dobEditText = findViewById(R.id.dobEditText);
        // Инициализация поля dobEditText.
        // "dobEditText" — переменная.
        // "findViewById" — метод.
        // "R.id.dobEditText" — идентификатор ресурса.
        // ";" — конец инструкции.

        phoneEditText = findViewById(R.id.phoneEditText);
        // Инициализация поля phoneEditText.
        //  "phoneEditText" — переменная.
        // "findViewById" — метод.
        // "R.id.phoneEditText" — идентификатор ресурса.
        // ";" — конец инструкции.

        emailEditText = findViewById(R.id.emailEditText);
        // Инициализация поля emailEditText.
        // "emailEditText" — переменная.
        // "findViewById" — метод.
        // "R.id.emailEditText" — идентификатор ресурса.
        // ";" — конец инструкции.

        addressEditText = findViewById(R.id.addressEditText);
        // Инициализация поля addressEditText.
        // "addressEditText" — переменная.
        // "findViewById" — метод.
        // "R.id.addressEditText" — идентификатор ресурса.
        // ";" — конец инструкции.

        diagnosisEditText = findViewById(R.id.diagnosisEditText);
        // Инициализация поля diagnosisEditText.
        // "diagnosisEditText" — переменная.
        // "findViewById" — метод.
        // "R.id.diagnosisEditText" — идентификатор ресурса.
        // ";" — конец инструкции.

        treatmentEditText = findViewById(R.id.treatmentEditText);
        // Инициализация поля treatmentEditText.
        // "treatmentEditText" — переменная.
        // "findViewById" — метод.
        // "R.id.treatmentEditText" — идентификатор ресурса.
        // ";" — конец инструкции.

        medicationsEditText = findViewById(R.id.medicationsEditText);
        // Инициализация поля medicationsEditText.
        // "medicationsEditText" — переменная.
        // "findViewById" — метод.
        // "R.id.medicationsEditText" — идентификатор ресурса.
        // ";" — конец инструкции.

        wardEditText = findViewById(R.id.wardEditText);
        // Инициализация поля wardEditText.
        // "wardEditText" — переменная.
        // "findViewById" — метод.
        // "R.id.wardEditText" — идентификатор ресурса.
        // ";" — конец инструкции.

        admissionDateEditText = findViewById(R.id.admissionDateEditText);
        // Инициализация поля admissionDateEditText.
        // "admissionDateEditText" — переменная.
        // "findViewById" — метод.
        // "R.id.admissionDateEditText" — идентификатор ресурса.
        // ";" — конец инструкции.

        doctorEditText = findViewById(R.id.doctorEditText);
        // Инициализация поля doctorEditText.
        // "doctorEditText" — переменная.
        // "findViewById" — метод.
        // "R.id.doctorEditText" — идентификатор ресурса.
        // ";" — конец инструкции.

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        // Получение текущей даты и форматирование её.
        // "String" — тип переменной, строка.
        // "currentDate" — имя переменной, будет хранить текущую дату.
        // "=" — оператор присваивания.
        // "new" — ключевое слово, создаёт новый объект.
        // "SimpleDateFormat" — класс, форматирует дату.
        // "yyyy-MM-dd" — строка, шаблон формата даты (год-месяц-день).
        // "," — разделитель параметров.
        // "Locale.getDefault" — метод класса Locale, возвращает текущую локаль устройства.
        // "()" — вызов метода без параметров.
        // "format" — метод класса SimpleDateFormat, форматирует дату.
        // "new Date()" — создание объекта Date, представляющего текущую дату и время.
        // ";" — конец инструкции.

        admissionDateEditText.setText(currentDate);
        // Установка текущей даты в поле admissionDateEditText.
        // "admissionDateEditText" — объект EditText.
        // "setText" — метод класса EditText, задаёт текст в поле.
        // "currentDate" — строка, текущая дата.
        // ";" — конец инструкции.

        doctorEditText.setKeyListener(null);
        // Отключение ввода текста в поле doctorEditText.
        // "doctorEditText" — объект EditText.
        // "setKeyListener" — метод класса EditText, задаёт слушатель клавиш.
        // "null" — значение, отключает ввод текста (поле становится только для чтения).
        // ";" — конец инструкции.

        diagnosisEditText.setKeyListener(null);
        // Отключение ввода текста в поле diagnosisEditText.
        // "diagnosisEditText" — объект EditText.
        // "setKeyListener" — метод.
        // "null" — отключает ввод текста.
        // ";" — конец инструкции.

        doctorEditText.setOnTouchListener((v, event) -> {
            // Установка слушателя касаний для doctorEditText.
            // "doctorEditText" — объект EditText.
            // "setOnTouchListener" — метод класса EditText, задаёт обработчик касаний.
            // "(v, event)" — параметры лямбда-выражения:
            // "v" — объект View, на котором произошло касание.
            // "event" — объект MotionEvent, событие касания.
            // "->" — оператор лямбда-выражения, разделяет параметры и тело.
            // "{" — открывающая фигурная скобка, начало тела лямбда-выражения.

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Проверка, что произошло нажатие на экран.
                // "if" — ключевое слово для условного оператора.
                // "event" — объект MotionEvent.
                // "getAction" — метод класса MotionEvent, возвращает тип действия.
                // "==" — оператор сравнения, проверяет равенство.
                // "MotionEvent.ACTION_DOWN" — константа, обозначает нажатие на экран.
                // "{" — открывающая фигурная скобка, начало тела условия.

                showSpecializationDialog();
                // Вызов метода для показа диалога выбора специальности.
                // "showSpecializationDialog" — метод, определённый ниже.
                // "()" — вызов метода без параметров.
                // ";" — конец инструкции.

                return true;
                // Возврат значения true.
                // "return" — ключевое слово, возвращает значение из лямбда-выражения.
                // "true" — булево значение, указывает, что событие обработано.
                // ";" — конец инструкции.
            }
            // "}" — закрывающая фигурная скобка, конец тела условия.

            return false;
            // Возврат значения false, если событие не обработано.
            // "return" — ключевое слово.
            // "false" — булево значение, указывает, что событие не обработано.
            // ";" — конец инструкции.
        });

        diagnosisEditText.setOnTouchListener((v, event) -> {
            // Установка слушателя касаний для diagnosisEditText.
            // "diagnosisEditText" — объект EditText.
            // "setOnTouchListener" — метод.
            // "(v, event)" — параметры лямбда-выражения.
            // "->" — оператор лямбда-выражения.
            // "{" — начало тела лямбда-выражения.

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Проверка нажатия на экран.
                // "if" — условный оператор.
                // "event.getAction()" — метод, возвращает тип действия.
                // "==" — оператор сравнения.
                // "MotionEvent.ACTION_DOWN" — константа, нажатие.
                // "{" — начало тела условия.

                showDiagnosisDialog();
                // Вызов метода для показа диалога выбора диагноза.
                // "showDiagnosisDialog" — метод, определённый ниже.
                // "()" — вызов метода.
                // ";" — конец инструкции.

                return true;
                // Возврат true.
                // "return" — ключевое слово.
                // "true" — событие обработано.
                // ";" — конец инструкции.
            }
            // "}" — конец тела условия.

            return false;
            // Возврат false.
            // "return" — ключевое слово.
            // "false" — событие не обработано.
            // ";" — конец инструкции.
        });

        doctorList = new ArrayList<>();
        // Инициализация списка doctorList.
        // "doctorList" — переменная.
        // "=" — оператор присваивания.
        // "new" — ключевое слово, создаёт новый объект.
        // "ArrayList" — класс, реализация списка.
        // "<>" — обобщение, указывает, что список содержит объекты Doctor.
        // "()" — вызов конструктора.
        // ";" — конец инструкции.

        initializeSpecializationDiagnoses();
        // Вызов метода для инициализации соответствия специальностей и диагнозов.
        // "initializeSpecializationDiagnoses" — метод, определённый ниже.
        // "()" — вызов метода.
        // ";" — конец инструкции.

        loadDoctors();
        // Вызов метода для загрузки врачей из базы данных.
        // "loadDoctors" — метод, определённый ниже.
        // "()" — вызов метода.
        // ";" — конец инструкции.

        Button saveButton = findViewById(R.id.saveButton);
        // Инициализация кнопки saveButton.
        // "Button" — тип переменной, виджет кнопки.
        // "saveButton" — имя переменной.
        // "=" — оператор присваивания.
        // "findViewById" — метод, находит элемент.
        // "R.id.saveButton" — идентификатор ресурса.
        // ";" — конец инструкции.

        saveButton.setOnClickListener(v -> savePatient());
        // Установка слушателя кликов для кнопки saveButton.
        // "saveButton" — объект Button.
        // "setOnClickListener" — метод класса Button, задаёт обработчик кликов.
        // "v" — параметр лямбда-выражения, объект View, на котором произошёл клик.
        // "->" — оператор лямбда-выражения.
        // "savePatient" — метод, определённый ниже, сохраняет пациента.
        // "()" — вызов метода.
        // ";" — конец инструкции.
    }
    // "}" — закрывающая фигурная скобка, конец метода onCreate.

    private void initializeSpecializationDiagnoses() {
        // Метод для инициализации соответствия специальностей и диагнозов.
        // "private" — модификатор доступа, делает метод доступным только внутри класса.
        // "void" — тип возвращаемого значения, метод ничего не возвращает.
        // "initializeSpecializationDiagnoses" — имя метода.
        // "()" — метод без параметров.
        // "{" — начало тела метода.

        specializationDiagnoses = new HashMap<>();
        // Инициализация переменной specializationDiagnoses.
        // "specializationDiagnoses" — переменная.
        // "=" — оператор присваивания.
        // "new" — ключевое слово, создаёт новый объект.
        // "HashMap" — класс, реализация ассоциативного массива.
        // "<>" — обобщение, ключ и значение — строки и списки строк.
        // "()" — вызов конструктора.
        // ";" — конец инструкции.

        specializationDiagnoses.put("Терапевт", Arrays.asList("ОРВИ и грипп", "Бронхит", "Гастрит", "Гипертония", "Анемия"));
        // Добавление диагнозов для специальности "Терапевт".
        // "specializationDiagnoses" — объект HashMap.
        // "put" — метод класса HashMap, добавляет пару ключ-значение.
        // "Терапевт" — строка, ключ (специальность).
        // "," — разделитель параметров.
        // "Arrays.asList" — метод класса Arrays, преобразует массив в список.
        // "ОРВИ и грипп", "Бронхит", ... — строки, диагнозы.
        // ";" — конец инструкции.

        specializationDiagnoses.put("Невролог", Arrays.asList("Остеохондроз", "Межпозвоночная грыжа", "Вегетососудистая дистония (ВСД)", "Невралгия", "Мигрень"));
        // Добавление диагнозов для специальности "Невролог".
        // Аналогично предыдущей строке.

        specializationDiagnoses.put("Гинеколог", Arrays.asList("Эрозия шейки матки", "Кандидоз (молочница)", "Миома матки", "Поликистоз яичников", "Эндометриоз"));
        // Добавление диагнозов для специальности "Гинеколог".

        specializationDiagnoses.put("Кардиолог", Arrays.asList("Гипертоническая болезнь", "Ишемическая болезнь сердца (ИБС)", "Аритмия", "Сердечная недостаточность", "Перикардит"));
        // Добавление диагнозов для специальности "Кардиолог".

        specializationDiagnoses.put("Хирург", Arrays.asList("Грыжа (паховая, пупочная)", "Варикозное расширение вен", "Аппендицит", "Гнойные абсцессы", "Переломы пальца(ев)", "Переломы запястья", "Перелом лучевой кости", "Перелом ключицы", "Перелом нижней челюсти", "Перелом левого бедра", "Перелом правого бедра", "Перелом левых костей голени", "Перелом правых костей голени"));
        // Добавление диагнозов для специальности "Хирург".

        specializationDiagnoses.put("Онколог", Arrays.asList("Рак молочной железы", "Рак легких", "Лейкоз (рак крови)", "Меланома", "Лимфома"));
        // Добавление диагнозов для специальности "Онколог".

        specializationDiagnoses.put("Педиатр", Arrays.asList("Диатез", "Коклюш", "Корь", "Рахит", "Детские инфекции (ветрянка, краснуха)"));
        // Добавление диагнозов для специальности "Педиатр".

        specializationDiagnoses.put("Стоматолог", Arrays.asList("Кариес", "Пульпит", "Пародонтоз", "Гингивит", "Флюс (периостит)"));
        // Добавление диагнозов для специальности "Стоматолог".

        specializationDiagnoses.put("Фармацевт", Arrays.asList("Головная боль", "Аллергия", "Боль в суставах", "Желудочно-кишечные расстройства"));
        // Добавление диагнозов для специальности "Фармацевт".
    }

    private void updateDiagnosisList(String specialization) {
        // Метод для обновления списка диагнозов на основе специальности.
        // "private" — модификатор доступа.
        // "void" — тип возвращаемого значения.
        // "updateDiagnosisList" — имя метода.
        // "String" — тип параметра, строка.
        // "specialization" — имя параметра, специальность врача.
        // "{" — начало тела метода.

        List<String> diagnoses = specializationDiagnoses.get(specialization);
        // Получение списка диагнозов для указанной специальности.
        // "List<String>" — тип переменной, список строк.
        // "diagnoses" — имя переменной.
        // "=" — оператор присваивания.
        // "specializationDiagnoses" — объект HashMap.
        // "get" — метод класса HashMap, возвращает значение по ключу.
        // "specialization" — строка, ключ (специальность).
        // ";" — конец инструкции.

        if (diagnoses != null) {
            // Проверка, что список диагнозов не пустой.
            // "if" — условный оператор.
            // "diagnoses" — переменная.
            // "!=" — оператор сравнения, проверяет, не равно ли значение null.
            // "null" — ключевое слово, обозначает отсутствие значения.
            // "{" — начало тела условия.

            currentDiagnoses = diagnoses.toArray(new String[0]);
            // Преобразование списка диагнозов в массив.
            // "currentDiagnoses" — переменная.
            // "=" — оператор присваивания.
            // "diagnoses" — список диагнозов.
            // "toArray" — метод класса List, преобразует список в массив.
            // "new String[0]" — создание пустого массива строк для результата.
            // ";" — конец инструкции.
        } else {
            // "}" — конец тела условия.
            // "else" — ключевое слово, выполняется, если условие if ложно.
            // "{" — начало тела else.

            currentDiagnoses = new String[0];
            // Установка пустого массива, если диагнозы не найдены.
            // "currentDiagnoses" — переменная.
            // "=" — оператор присваивания.
            // "new" — ключевое слово.
            // "String[0]" — создание пустого массива строк.
            // ";" — конец инструкции.
        }
        // "}" — конец тела else.
    }

    private void showSpecializationDialog() {
        // Метод для показа диалога выбора специальности.
        // "private" — модификатор доступа.
        // "void" — тип возвращаемого значения.
        // "showSpecializationDialog" — имя метода.
        // "()" — метод без параметров.
        // "{" — начало тела метода.

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Создание объекта для построения диалога.
        // "AlertDialog.Builder" — класс для создания диалога.
        // "builder" — имя переменной.
        // "=" — оператор присваивания.
        // "new" — ключевое слово.
        // "AlertDialog.Builder" — класс.
        // "this" — текущий объект активности (контекст).
        // ";" — конец инструкции.

        builder.setTitle("Выберите специальность врача");
        // Установка заголовка диалога.
        // "builder" — объект AlertDialog.Builder.
        // "setTitle" — метод, задаёт заголовок.
        // "Выберите специальность врача" — строка, текст заголовка.
        // ";" — конец инструкции.

        builder.setItems(specializations, (dialog, which) -> {
            // Установка списка специальностей и обработчика выбора.
            // "builder" — объект AlertDialog.Builder.
            // "setItems" — метод, задаёт список элементов и обработчик.
            // "specializations" — массив строк, список специальностей.
            // "," — разделитель параметров.
            // "(dialog, which)" — параметры лямбда-выражения:
            // "dialog" — объект DialogInterface.
            // "which" — целое число, индекс выбранного элемента.
            // "->" — оператор лямбда-выражения.
            // "{" — начало тела лямбда-выражения.

            String selectedSpecialization = specializations[which];
            // Получение выбранной специальности.
            // "String" — тип переменной.
            // "selectedSpecialization" — имя переменной.
            // "=" — оператор присваивания.
            // "specializations" — массив специальностей.
            // "which" — индекс выбранного элемента.
            // ";" — конец инструкции.

            showDoctorsDialog(selectedSpecialization);
            // Вызов метода для показа диалога выбора врача.
            // "showDoctorsDialog" — метод, определённый ниже.
            // "selectedSpecialization" — строка, выбранная специальность.
            // ";" — конец инструкции.
        });
        // "}" — конец тела лямбда-выражения.
        // ");" — конец инструкции.

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
        // Установка кнопки "Отмена".
        // "builder" — объект AlertDialog.Builder.
        // "setNegativeButton" — метод, задаёт отрицательную кнопку.
        // "Отмена" — строка, текст кнопки.
        // "," — разделитель параметров.
        // "(dialog, which)" — параметры лямбда-выражения.
        // "dialog.dismiss" — метод, закрывает диалог.
        // ";" — конец инструкции.

        builder.create().show();
        // Создание и показ диалога.
        // "builder" — объект AlertDialog.Builder.
        // "create" — метод, создаёт объект AlertDialog.
        // "show" — метод класса AlertDialog, показывает диалог.
        // ";" — конец инструкции.
    }
    // "}" — конец метода.

    private void showDoctorsDialog(String specialization) {
        // Метод для показа диалога выбора врача по специальности.
        // "private" — модификатор доступа.
        // "void" — тип возвращаемого значения.
        // "showDoctorsDialog" — имя метода.
        // "String" — тип параметра.
        // "specialization" — имя параметра, специальность врача.
        // "{" — начало тела метода.

        List<Doctor> doctorsBySpecialization = new ArrayList<>();
        // Создание списка врачей по специальности.
        // "List<Doctor>" — тип переменной, список объектов Doctor.
        // "doctorsBySpecialization" — имя переменной.
        // "=" — оператор присваивания.
        // "new" — ключевое слово.
        // "ArrayList" — класс.
        // "<>" — обобщение.
        // "()" — вызов конструктора.
        // ";" — конец инструкции.

        for (Doctor doctor : doctorList) {
            // Цикл по списку врачей.
            // "for" — ключевое слово для цикла.
            // "Doctor" — тип переменной цикла.
            // "doctor" — имя переменной цикла, каждый врач из списка.
            // ":" — разделитель в цикле for-each.
            // "doctorList" — список врачей.
            // "{" — начало тела цикла.

            if (doctor.getSpecialization().equals(specialization)) {
                // Проверка, соответствует ли специальность врача.
                // "if" — условный оператор.
                // "doctor" — объект Doctor.
                // "getSpecialization" — метод, возвращает специальность врача.
                // "equals" — метод класса String, сравнивает строки.
                // "specialization" — строка, специальность для фильтрации.
                // "{" — начало тела условия.

                doctorsBySpecialization.add(doctor);
                // Добавление врача в список.
                // "doctorsBySpecialization" — список.
                // "add" — метод класса List, добавляет элемент.
                // "doctor" — объект Doctor.
                // ";" — конец инструкции.
            }
            // "}" — конец тела условия.
        }
        // "}" — конец цикла.

        if (doctorsBySpecialization.isEmpty()) {
            // Проверка, пуст ли список врачей.
            // "if" — условный оператор.
            // "doctorsBySpecialization" — список.
            // "isEmpty" — метод класса List, возвращает true, если список пуст.
            // "()" — вызов метода.
            // "{" — начало тела условия.

            Toast.makeText(this, "Нет врачей с этой специальностью", Toast.LENGTH_SHORT).show();
            // Показ уведомления, если врачей нет.
            // "Toast" — класс для уведомлений.
            // "makeText" — статический метод класса Toast, создаёт уведомление.
            // "this" — текущий контекст.
            // "," — разделитель параметров.
            // "Нет врачей с этой специальностью" — строка, текст уведомления.
            // "Toast.LENGTH_SHORT" — константа, длительность показа (короткая).
            // "show" — метод класса Toast, показывает уведомление.
            // ";" — конец инструкции.

            return;
            // Выход из метода.
            // "return" — ключевое слово, завершает выполнение метода.
            // ";" — конец инструкции.
        }
        // "}" — конец тела условия.

        String[] doctorNames = new String[doctorsBySpecialization.size()];
        // Создание массива имён врачей.
        // "String[]" — тип переменной, массив строк.
        // "doctorNames" — имя переменной.
        // "=" — оператор присваивания.
        // "new" — ключевое слово.
        // "String" — тип элементов массива.
        // "doctorsBySpecialization" — список врачей.
        // "size" — метод класса List, возвращает количество элементов.
        // "()" — вызов метода.
        // ";" — конец инструкции.

        for (int i = 0; i < doctorsBySpecialization.size(); i++) {
            // Цикл для заполнения массива имён врачей.
            // "for" — ключевое слово для цикла.
            // "int" — тип переменной, целое число.
            // "i" — имя переменной, счётчик цикла.
            // "=" — оператор присваивания.
            // "0" — начальное значение счётчика.
            // ";" — разделитель условий цикла.
            // "i < doctorsBySpecialization.size()" — условие цикла, выполняется, пока i меньше размера списка.
            // "i++" — инкремент, увеличивает i на 1.
            // "{" — начало тела цикла.

            Doctor doctor = doctorsBySpecialization.get(i);
            // Получение врача по индексу.
            // "Doctor" — тип переменной.
            // "doctor" — имя переменной.
            // "=" — оператор присваивания.
            // "doctorsBySpecialization" — список.
            // "get" — метод класса List, возвращает элемент по индексу.
            // "i" — индекс.
            // ";" — конец инструкции.

            doctorNames[i] = doctor.getLastName() + " " + doctor.getFirstName() + " " + doctor.getMiddleName();
            // Формирование строки с ФИО врача.
            // "doctorNames" — массив.
            // "i" — индекс.
            // "=" — оператор присваивания.
            // "doctor" — объект Doctor.
            // "getLastName" — метод, возвращает фамилию.
            // "+" — оператор конкатенации строк.
            // " " — пробел.
            // "getFirstName" — метод, возвращает имя.
            // "getMiddleName" — метод, возвращает отчество.
            // ";" — конец инструкции.
        }
        // "}" — конец цикла.

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Создание объекта для построения диалога.
        // "AlertDialog.Builder" — класс.
        // "builder" — имя переменной.
        // "=" — оператор присваивания.
        // "new" — ключевое слово.
        // "AlertDialog.Builder" — класс.
        // "this" — контекст.
        // ";" — конец инструкции.

        builder.setTitle("Выберите врача (" + specialization + ")");
        // Установка заголовка диалога.
        // "builder" — объект.
        // "setTitle" — метод.
        // "Выберите врача (" — строка, начало заголовка.
        // "+" — конкатенация.
        // "specialization" — строка, специальность.
        // ")" — закрывающая скобка.
        // ";" — конец инструкции.

        builder.setItems(doctorNames, (dialog, which) -> {
            // Установка списка врачей и обработчика выбора.
            // "builder" — объект.
            // "setItems" — метод.
            // "doctorNames" — массив имён врачей.
            // "," — разделитель.
            // "(dialog, which)" — параметры лямбда-выражения.
            // "->" — оператор лямбда-выражения.
            // "{" — начало тела лямбда-выражения.

            selectedDoctor = doctorsBySpecialization.get(which);
            // Сохранение выбранного врача.
            // "selectedDoctor" — переменная.
            // "=" — оператор присваивания.
            // "doctorsBySpecialization" — список.
            // "get" — метод, возвращает элемент.
            // "which" — индекс выбранного врача.
            // ";" — конец инструкции.

            doctorEditText.setText(selectedDoctor.getLastName() + " " + selectedDoctor.getFirstName() + " " + selectedDoctor.getMiddleName());
            // Установка ФИО врача в поле doctorEditText.
            // "doctorEditText" — объект EditText.
            // "setText" — метод.
            // "selectedDoctor" — объект Doctor.
            // "getLastName" — метод.
            // "+" — конкатенация.
            // " " — пробел.
            // "getFirstName" — метод.
            // "getMiddleName" — метод.
            // ";" — конец инструкции.

            updateDiagnosisList(selectedDoctor.getSpecialization());
            // Обновление списка диагнозов.
            // "updateDiagnosisList" — метод.
            // "selectedDoctor" — объект Doctor.
            // "getSpecialization" — метод, возвращает специальность.
            // ";" — конец инструкции.

            diagnosisEditText.setText("");
            // Очистка поля diagnosisEditText.
            // "diagnosisEditText" — объект EditText.
            // "setText" — метод.
            // "" — пустая строка.
            // ";" — конец инструкции.
        });
        // "}" — конец тела лямбда-выражения.
        // ");" — конец инструкции.

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
        // Установка кнопки "Отмена".
        // "builder" — объект.
        // "setNegativeButton" — метод.
        // "Отмена" — текст кнопки.
        // "," — разделитель.
        // "(dialog, which)" — параметры лямбда-выражения.
        // "dialog.dismiss" — метод, закрывает диалог.
        // ";" — конец инструкции.

        builder.create().show();
        // Создание и показ диалога.
        // "builder" — объект.
        // "create" — метод.
        // "show" — метод.
        // ";" — конец инструкции.
    }
    // "}" — конец метода.

    private void showDiagnosisDialog() {
        // Метод для показа диалога выбора диагноза.
        // "private" — модификатор доступа.
        // "void" — тип возвращаемого значения.
        // "showDiagnosisDialog" — имя метода.
        // "()" — метод без параметров.
        // "{" — начало тела метода.

        if (selectedDoctor == null) {
            // Проверка, выбран ли врач.
            // "if" — условный оператор.
            // "selectedDoctor" — переменная.
            // "==" — оператор сравнения.
            // "null" — отсутствие значения.
            // "{" — начало тела условия.

            Toast.makeText(this, "Сначала выберите врача", Toast.LENGTH_SHORT).show();
            // Показ уведомления, если врач не выбран.
            // "Toast" — класс.
            // "makeText" — метод.
            // "this" — контекст.
            // "Сначала выберите врача" — текст.
            // "Toast.LENGTH_SHORT" — длительность.
            // "show" — метод.
            // ";" — конец инструкции.

            return;
            // Выход из метода.
            // "return" — ключевое слово.
            // ";" — конец инструкции.
        }
        // "}" — конец тела условия.

        if (currentDiagnoses.length == 0) {
            // Проверка, есть ли доступные диагнозы.
            // "if" — условный оператор.
            // "currentDiagnoses" — массив.
            // "length" — поле, возвращает длину массива.
            // "==" — оператор сравнения.
            // "0" — значение.
            // "{" — начало тела условия.

            Toast.makeText(this, "Нет доступных диагнозов для этой специальности", Toast.LENGTH_SHORT).show();
            // Показ уведомления, если диагнозов нет.
            // "Toast" — класс.
            // "makeText" — метод.
            // "this" — контекст.
            // "Нет доступных диагнозов для этой специальности" — текст.
            // "Toast.LENGTH_SHORT" — длительность.
            // "show" — метод.
            // ";" — конец инструкции.

            return;
            // Выход из метода.
            // "return" — ключевое слово.
            // ";" — конец инструкции.
        }
        // "}" — конец тела условия.

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Создание объекта для построения диалога.
        // "AlertDialog.Builder" — класс.
        // "builder" — имя переменной.
        // "=" — оператор присваивания.
        // "new" — ключевое слово.
        // "AlertDialog.Builder" — класс.
        // "this" — контекст.
        // ";" — конец инструкции.

        builder.setTitle("Выберите диагноз");
        // Установка заголовка диалога.
        // "builder" — объект.
        // "setTitle" — метод.
        // "Выберите диагноз" — текст.
        // ";" — конец инструкции.

        builder.setItems(currentDiagnoses, (dialog, which) -> {
            // Установка списка диагнозов и обработчика выбора.
            // "builder" — объект.
            // "setItems" — метод.
            // "currentDiagnoses" — массив диагнозов.
            // "," — разделитель.
            // "(dialog, which)" — параметры лямбда-выражения.
            // "->" — оператор лямбда-выражения.
            // "{" — начало тела лямбда-выражения.

            diagnosisEditText.setText(currentDiagnoses[which]);
            // Установка выбранного диагноза в поле.
            // "diagnosisEditText" — объект EditText.
            // "setText" — метод.
            // "currentDiagnoses" — массив.
            // "which" — индекс выбранного диагноза.
            // ";" — конец инструкции.
        });
        // "}" — конец тела лямбда-выражения.
        // ");" — конец инструкции.

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
        // Установка кнопки "Отмена".
        // "builder" — объект.
        // "setNegativeButton" — метод.
        // "Отмена" — текст.
        // "," — разделитель.
        // "(dialog, which)" — параметры лямбда-выражения.
        // "dialog.dismiss" — метод.
        // ";" — конец инструкции.

        builder.create().show();
        // Создание и показ диалога.
        // "builder" — объект.
        // "create" — метод.
        // "show" — метод.
        // ";" — конец инструкции.
    }
    // "}" — конец метода.

    private void loadDoctors() {
        // Метод для загрузки врачей из базы данных.
        // "private" — модификатор доступа.
        // "void" — тип возвращаемого значения.
        // "loadDoctors" — имя метода.
        // "()" — метод без параметров.
        // "{" — начало тела метода.

        doctorList.clear();
        // Очистка списка врачей.
        // "doctorList" — список.
        // "clear" — метод класса List, удаляет все элементы.
        // "()" — вызов метода.
        // ";" — конец инструкции.

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Получение объекта базы данных для чтения.
        // "SQLiteDatabase" — тип переменной.
        // "db" — имя переменной.
        // "=" — оператор присваивания.
        // "dbHelper" — объект HospitalDbHelper.
        // "getReadableDatabase" — метод, возвращает базу данных.
        // "()" — вызов метода.
        // ";" — конец инструкции.

        Cursor cursor = db.query("Doctors", null, null, null, null, null, null);
        // Выполнение запроса к таблице Doctors.
        // "Cursor" — тип переменной.
        // "cursor" — имя переменной.
        // "=" — оператор присваивания.
        // "db" — объект базы данных.
        // "query" — метод, выполняет SQL-запрос.
        // "Doctors" — имя таблицы.
        // "null" — все столбцы.
        // "null" — условие WHERE (отсутствует).
        // "null" — значения для заполнителей (отсутствуют).
        // "null" — groupBy (отсутствует).
        // "null" — having (отсутствует).
        // "null" — orderBy (отсутствует).
        // ";" — конец инструкции.

        while (cursor.moveToNext()) {
            // Цикл по результатам запроса.
            // "while" — ключевое слово для цикла.
            // "cursor" — объект Cursor.
            // "moveToNext" — метод, переходит к следующей строке.
            // "()" — вызов метода.
            // "{" — начало тела цикла.

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
            // Извлечение идентификатора врача.
            // "int" — тип переменной.
            // "id" — имя переменной.
            // "=" — оператор присваивания.
            // "cursor" — объект Cursor.
            // "getInt" — метод, извлекает целое число.
            // "getColumnIndexOrThrow" — метод, возвращает индекс столбца.
            // "doctor_id" — имя столбца.
            // ";" — конец инструкции.

            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
            // Извлечение имени врача.
            // "String" — тип переменной.
            // "firstName" — имя переменной.
            // "getString" — метод, извлекает строку.
            // "getColumnIndexOrThrow" — метод.
            // "first_name" — имя столбца.
            // ";" — конец инструкции.

            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            // Извлечение фамилии врача.
            // "lastName" — имя переменной.
            // "getString" — метод.
            // "last_name" — имя столбца.
            // ";" — конец инструкции.

            String middleName = cursor.getString(cursor.getColumnIndexOrThrow("middle_name"));
            // Извлечение отчества врача.
            // "middleName" — имя переменной.
            // "getString" — метод.
            // "middle_name" — имя столбца.
            // ";" — конец инструкции.

            String specialization = cursor.getString(cursor.getColumnIndexOrThrow("specialization"));
            // Извлечение специальности врача.
            // "specialization" — имя переменной.
            // "getString" — метод.
            // "specialization" — имя столбца.
            // ";" — конец инструкции.

            doctorList.add(new Doctor(id, firstName, lastName, middleName, specialization));
            // Добавление врача в список.
            // "doctorList" — список.
            // "add" — метод.
            // "new" — ключевое слово.
            // "Doctor" — класс.
            // "id, firstName, lastName, middleName, specialization" — параметры конструктора.
            // ";" — конец инструкции.
        }
        // "}" — конец цикла.

        cursor.close();
        // Закрытие курсора.
        // "cursor" — объект Cursor.
        // "close" — метод, освобождает ресурсы.
        // "()" — вызов метода.
        // ";" — конец инструкции.

        if (doctorList.isEmpty()) {
            // Проверка, пуст ли список врачей.
            // "if" — условный оператор.
            // "doctorList" — список.
            // "isEmpty" — метод.
            // "()" — вызов метода.
            // "{" — начало тела условия.

            Toast.makeText(this, "Список врачей пуст. Добавьте врача перед добавлением пациента.", Toast.LENGTH_LONG).show();
            // Показ уведомления, если врачей нет.
            // "Toast" — класс.
            // "makeText" — метод.
            // "this" — контекст.
            // "Список врачей пуст. Добавьте врача перед добавлением пациента." — текст.
            // "Toast.LENGTH_LONG" — длительность (длинная).
            // "show" — метод.
            // ";" — конец инструкции.
        }
        // "}" — конец тела условия.
    }
    // "}" — конец метода.

    private void savePatient() {
        // Метод для сохранения пациента в базу данных.
        // "private" — модификатор доступа.
        // "void" — тип возвращаемого значения.
        // "savePatient" — имя метода.
        // "()" — метод без параметров.
        // "{" — начало тела метода.

        String firstName = firstNameEditText.getText().toString().trim();
        // Получение имени пациента.
        // "String" — тип переменной.
        // "firstName" — имя переменной.
        // "=" — оператор присваивания.
        // "firstNameEditText" — объект EditText.
        // "getText" — метод, возвращает текст.
        // "toString" — метод, преобразует в строку.
        // "trim" — метод, удаляет пробелы в начале и конце.
        // ";" — конец инструкции.

        String lastName = lastNameEditText.getText().toString().trim();
        // Получение фамилии пациента.
        // "lastName" — имя переменной.
        // "lastNameEditText" — объект EditText.
        // "getText" — метод.
        // "toString" — метод.
        // "trim" — метод.
        // ";" — конец инструкции.

        String middleName = middleNameEditText.getText().toString().trim();
        // Получение отчества пациента.
        // "middleName" — имя переменной.
        // "middleNameEditText" — объект EditText.
        // "getText" — метод.
        // "toString" — метод.
        // "trim" — метод.
        // ";" — конец инструкции.

        String dob = dobEditText.getText().toString().trim();
        // Получение даты рождения.
        // "dob" — имя переменной.
        // "dobEditText" — объект EditText.
        // "getText" — метод.
        // "toString" — метод.
        // "trim" — метод.
        // ";" — конец инструкции.

        String phone = phoneEditText.getText().toString().trim();
        // Получение телефона.
        // "phone" — имя переменной.
        // "phoneEditText" — объект EditText.
        // "getText" — метод.
        // "toString" — метод.
        // "trim" — метод.
        // ";" — конец инструкции.

        String email = emailEditText.getText().toString().trim();
        // Получение email.
        // "email" — имя переменной.
        // "emailEditText" — объект EditText.
        // "getText" — метод.
        // "toString" — метод.
        // "trim" — метод.
        // ";" — конец инструкции.

        String address = addressEditText.getText().toString().trim();
        // Получение адреса.
        // "address" — имя переменной.
        // "addressEditText" — объект EditText.
        // "getText" — метод.
        // "toString" — метод.
        // "trim" — метод.
        // ";" — конец инструкции.

        String diagnosis = diagnosisEditText.getText().toString().trim();
        // Получение диагноза.
        // "diagnosis" — имя переменной.
        // "diagnosisEditText" — объект EditText.
        // "getText" — метод.
        // "toString" — метод.
        // "trim" — метод.
        // ";" — конец инструкции.

        String treatment = treatmentEditText.getText().toString().trim();
        // Получение лечения.
        // "treatment" — имя переменной.
        // "treatmentEditText" — объект EditText.
        // "getText" — метод.
        // "toString" — метод.
        // "trim" — метод.
        // ";" — конец инструкции.

        String medications = medicationsEditText.getText().toString().trim();
        // Получение лекарств.
        // "medications" — имя переменной.
        // "medicationsEditText" — объект EditText.
        // "getText" — метод.
        // "toString" — метод.
        // "trim" — метод.
        // ";" — конец инструкции.

        String ward = wardEditText.getText().toString().trim();
        // Получение палаты.
        // "ward" — имя переменной.
        // "wardEditText" — объект EditText.
        // "getText" — метод.
        // "toString" — метод.
        // "trim" — метод.
        // ";" — конец инструкции.

        String admissionDate = admissionDateEditText.getText().toString().trim();
        // Получение даты поступления.
        // "admissionDate" — имя переменной.
        // "admissionDateEditText" — объект EditText.
        // "getText" — метод.
        // "toString" — метод.
        // "trim" — метод.
        // ";" — конец инструкции.

        int doctorId = selectedDoctor != null ? selectedDoctor.getId() : -1;
        // Получение идентификатора врача.
        // "int" — тип переменной.
        // "doctorId" — имя переменной.
        // "=" — оператор присваивания.
        // "selectedDoctor" — переменная.
        // "!=" — оператор сравнения.
        // "null" — отсутствие значения.
        // "?" — тернарный оператор, если true, то первое выражение, иначе второе.
        // "selectedDoctor.getId()" — метод, возвращает id врача.
        // ":" — разделитель в тернарном операторе.
        // "-1" — значение по умолчанию, если врач не выбран.
        // ";" — конец инструкции.

        if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || admissionDate.isEmpty()) {
            // Проверка обязательных полей.
            // "if" — условный оператор.
            // "firstName" — строка.
            // "isEmpty" — метод класса String, возвращает true, если строка пустая.
            // "()" — вызов метода.
            // "||" — логический оператор ИЛИ.
            // "lastName" — строка.
            // "dob" — строка.
            // "admissionDate" — строка.
            // "{" — начало тела условия.

            Toast.makeText(this, "Заполните обязательные поля: имя, фамилия, дата рождения, дата поступления", Toast.LENGTH_SHORT).show();
            // Показ уведомления, если поля пустые.
            // "Toast" — класс.
            // "makeText" — метод.
            // "this" — контекст.
            // "Заполните обязательные поля: имя, фамилия, дата рождения, дата поступления" — текст.
            // "Toast.LENGTH_SHORT" — длительность.
            // "show" — метод.
            // ";" — конец инструкции.

            return;
            // Выход из метода.
            // "return" — ключевое слово.
            // ";" — конец инструкции.
        }
        // "}" — конец тела условия.

        if (selectedDoctor == null || doctorId == -1) {
            // Проверка, выбран ли врач.
            // "if" — условный оператор.
            // "selectedDoctor" — переменная.
            // "==" — оператор сравнения.
            // "null" — отсутствие значения.
            // "||" — логический оператор ИЛИ.
            // "doctorId" — переменная.
            // "==" — оператор сравнения.
            // "-1" — значение.
            // "{" — начало тела условия.

            Toast.makeText(this, "Выберите врача", Toast.LENGTH_SHORT).show();
            // Показ уведомления, если врач не выбран.
            // "Toast" — класс.
            // "makeText" — метод.
            // "this" — контекст.
            // "Выберите врача" — текст.
            // "Toast.LENGTH_SHORT" — длительность.
            // "show" — метод.
            // ";" — конец инструкции.

            return;
            // Выход из метода.
            // "return" — ключевое слово.
            // ";" — конец инструкции.
        }
        // "}" — конец тела условия.

        if (diagnosis.isEmpty()) {
            // Проверка, выбран ли диагноз.
            // "if" — условный оператор.
            // "diagnosis" — строка.
            // "isEmpty" — метод.
            // "()" — вызов метода.
            // "{" — начало тела условия.

            Toast.makeText(this, "Выберите диагноз", Toast.LENGTH_SHORT).show();
            // Показ уведомления, если диагноз не выбран.
            // "Toast" — класс.
            // "makeText" — метод.
            // "this" — контекст.
            // "Выберите диагноз" — текст.
            // "Toast.LENGTH_SHORT" — длительность.
            // "show" — метод.
            // ";" — конец инструкции.

            return;
            // Выход из метода.
            // "return" — ключевое слово.
            // ";" — конец инструкции.
        }
        // "}" — конец тела условия.

        if (!isValidDateFormat(admissionDate)) {
            // Проверка формата даты поступления.
            // "if" — условный оператор.
            // "!" — логический оператор НЕ.
            // "isValidDateFormat" — метод, определённый ниже, проверяет формат даты.
            // "admissionDate" — строка, дата поступления.
            // "{" — начало тела условия.

            Toast.makeText(this, "Некорректный формат даты поступления (ожидается YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
            // Показ уведомления, если формат даты некорректен.
            // "Toast" — класс.
            // "makeText" — метод.
            // "this" — контекст.
            // "Некорректный формат даты поступления (ожидается YYYY-MM-DD)" — текст.
            // "Toast.LENGTH_SHORT" — длительность.
            // "show" — метод.
            // ";" — конец инструкции.

            return;
            // Выход из метода.
            // "return" — ключевое слово.
            // ";" — конец инструкции.
        }
        // "}" — конец тела условия.

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Получение объекта базы данных для записи.
        // "SQLiteDatabase" — тип переменной.
        // "db" — имя переменной.
        // "=" — оператор присваивания.
        // "dbHelper" — объект HospitalDbHelper.
        // "getWritableDatabase" — метод, возвращает базу данных для записи.
        // "()" — вызов метода.
        // ";" — конец инструкции.

        ContentValues values = new ContentValues();
        // Создание объекта для хранения данных пациента.
        // "ContentValues" — тип переменной.
        // "values" — имя переменной.
        // "=" — оператор присваивания.
        // "new" — ключевое слово.
        // "ContentValues" — класс.
        // "()" — вызов конструктора.
        // ";" — конец инструкции.

        values.put("first_name", firstName);
        // Добавление имени пациента в ContentValues.
        // "values" — объект ContentValues.
        // "put" — метод, добавляет пару ключ-значение.
        // "first_name" — строка, ключ (имя столбца).
        // "firstName" — строка, значение.
        // ";" — конец инструкции.

        values.put("last_name", lastName);
        // Добавление фамилии пациента.
        // "last_name" — ключ.
        // "lastName" — значение.
        // ";" — конец инструкции.

        values.put("middle_name", middleName);
        // Добавление отчества пациента.
        // "middle_name" — ключ.
        // "middleName" — значение.
        // ";" — конец инструкции.

        values.put("date_of_birth", dob);
        // Добавление даты рождения.
        // "date_of_birth" — ключ.
        // "dob" — значение.
        // ";" — конец инструкции.

        values.put("phone", phone);
        // Добавление телефона.
        // "phone" — ключ.
        // "phone" — значение.
        // ";" — конец инструкции.

        values.put("email", email);
        // Добавление email.
        // "email" — ключ.
        // "email" — значение.
        // ";" — конец инструкции.

        values.put("address", address);
        // Добавление адреса.
        // "address" — ключ.
        // "address" — значение.
        // ";" — конец инструкции.

        values.put("doctor_id", doctorId);
        // Добавление идентификатора врача.
        // "doctor_id" — ключ.
        // "doctorId" — значение.
        // ";" — конец инструкции.

        values.put("diagnosis", diagnosis);
        // Добавление диагноза.
        // "diagnosis" — ключ.
        // "diagnosis" — значение.
        // ";" — конец инструкции.

        values.put("treatment", treatment);
        // Добавление лечения.
        // "treatment" — ключ.
        // "treatment" — значение.
        // ";" — конец инструкции.

        values.put("medications", medications);
        // Добавление лекарств.
        // "medications" — ключ.
        // "medications" — значение.
        // ";" — конец инструкции.

        values.put("ward", ward);
        // Добавление палаты.
        // "ward" — ключ.
        // "ward" — значение.
        // ";" — конец инструкции.

        values.put("admission_date", admissionDate);
        // Добавление даты поступления.
        // "admission_date" — ключ.
        // "admissionDate" — значение.
        // ";" — конец инструкции.

        values.put("admission_count", 1);
        // Добавление количества поступлений (по умолчанию 1).
        // "admission_count" — ключ.
        // "1" — значение.
        // ";" — конец инструкции.

        try {
            // Начало блока try-catch для обработки исключений.
            // "try" — ключевое слово, начало блока обработки исключений.
            // "{" — начало тела блока.

            long newRowId = db.insert("Patients", null, values);
            // Вставка данных пациента в таблицу Patients.
            // "long" — тип переменной, длинное целое число.
            // "newRowId" — имя переменной, будет хранить идентификатор новой строки.
            // "=" — оператор присваивания.
            // "db" — объект SQLiteDatabase.
            // "insert" — метод, вставляет данные в таблицу.
            // "Patients" — имя таблицы.
            // "null" — параметр, столбец для значения null (не используется).
            // "values" — объект ContentValues, данные для вставки.
            // ";" — конец инструкции.

            if (newRowId != -1) {
                // Проверка, успешно ли добавлена строка.
                // "if" — условный оператор.
                // "newRowId" — переменная.
                // "!=" — оператор сравнения.
                // "-1" — значение, возвращается при ошибке вставки.
                // "{" — начало тела условия.

                ContentValues historyValues = new ContentValues();
                // Создание объекта для хранения истории изменений.
                // "ContentValues" — тип переменной.
                // "historyValues" — имя переменной.
                // "=" — оператор присваивания.
                // "new" — ключевое слово.
                // "ContentValues" — класс.
                // "()" — вызов конструктора.
                // ";" — конец инструкции.

                historyValues.put("patient_id", newRowId);
                // Добавление идентификатора пациента в историю.
                // "historyValues" — объект ContentValues.
                // "put" — метод.
                // "patient_id" — ключ.
                // "newRowId" — значение.
                // ";" — конец инструкции.

                historyValues.put("diagnosis", diagnosis);
                // Добавление диагноза в историю.
                // "diagnosis" — ключ.
                // "diagnosis" — значение.
                // ";" — конец инструкции.

                historyValues.put("treatment", treatment);
                // Добавление лечения в историю.
                // "treatment" — ключ.
                // "treatment" — значение.
                // ";" — конец инструкции.

                historyValues.put("medications", medications);
                // Добавление лекарств в историю.
                // "medications" — ключ.
                // "medications" — значение.
                // ";" — конец инструкции.

                historyValues.put("ward", ward);
                // Добавление палаты в историю.
                // "ward" — ключ.
                // "ward" — значение.
                // ";" — конец инструкции.

                historyValues.put("update_date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                // Добавление даты обновления в историю.
                // "update_date" — ключ.
                // "new SimpleDateFormat" — создание объекта для форматирования даты.
                // "yyyy-MM-dd" — шаблон формата.
                // "Locale.getDefault()" — текущая локаль.
                // "format" — метод, форматирует дату.
                // "new Date()" — текущая дата.
                // ";" — конец инструкции.

                db.insert("PatientHistory", null, historyValues);
                // Вставка записи в таблицу PatientHistory.
                // "db" — объект SQLiteDatabase.
                // "insert" — метод.
                // "PatientHistory" — имя таблицы.
                // "null" — параметр.
                // "historyValues" — данные.
                // ";" — конец инструкции.

                Toast.makeText(this, "Пациент добавлен", Toast.LENGTH_SHORT).show();
                // Показ уведомления об успешном добавлении.
                // "Toast" — класс.
                // "makeText" — метод.
                // "this" — конт
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

    private boolean isValidDateFormat(String date) {
        try {
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}