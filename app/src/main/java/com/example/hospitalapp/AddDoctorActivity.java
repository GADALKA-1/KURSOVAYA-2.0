package com.example.hospitalapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddDoctorActivity extends AppCompatActivity {
    // Объявляем класс AddDoctorActivity, который наследуется от AppCompatActivity
// "public" — модификатор доступа, означает, что класс доступен из любого другого класса
// "class" — ключевое слово для объявления класса
// "AddDoctorActivity" — имя класса, отражает его назначение (активность для добавления врача)
// "extends" — ключевое слово, указывает, что наш класс наследуется от другого класса
// "AppCompatActivity" — базовый класс, от которого мы наследуемся, предоставляет функциональность активности
    private HospitalDbHelper dbHelper;
    // Объявляем переменную для работы с базой данных
    // "private" — модификатор доступа
    // "HospitalDbHelper" — тип переменной, наш класс для работы с базой данных
    // "dbHelper" — имя переменной, будет хранить объект для взаимодействия с базой данных
    private EditText firstNameEditText, lastNameEditText, middleNameEditText, specializationEditText;
    // Объявляем переменные для текстовых полей ввода
    // "private" — модификатор доступа
    // "EditText" — тип переменной, виджет для текстового поля ввода
    // "firstNameEditText" — имя переменной, поле для ввода имени врача
    // "lastNameEditText" — поле для ввода фамилии врача
    // "middleNameEditText" — поле для ввода отчества врача
    // "specializationEditText" — поле для ввода специальности врача

    private final String[] specializations = {
            "Терапевт", "Невролог", "Гинеколог", "Кардиолог", "Хирург",
            "Онколог", "Педиатр", "Стоматолог", "Фармацевт"
    };
    // Объявляем массив специальностей
    // "private" — модификатор доступа
    // "final" — значение массива нельзя изменить
    // "String[]" — тип переменной, массив строк
    // "specializations" — имя переменной, хранит список доступных специальностей
    // "=" — оператор присваивания
    // "{" — начало массива
    // "Терапевт", "Невролог", ... — элементы массива, строки с названиями специальностей
    // "}" — конец массива

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Переопределяем метод onCreate, который вызывается при создании активности
        // "@Override" — аннотация, указывает, что мы переопределяем метод из родительского класса (AppCompatActivity)
        // "protected" — модификатор доступа, метод доступен в этом классе и его подклассах
        // "void" — тип возвращаемого значения, метод ничего не возвращает
        // "onCreate" — имя метода, вызывается при создании активности
        // "Bundle" — тип параметра, объект для хранения данных (например, сохранённого состояния)
        // "savedInstanceState" — имя параметра, содержит сохранённое состояние активности (если она была ранее уничтожена и восстановлена)
        super.onCreate(savedInstanceState);
        // Вызываем метод onCreate родительского класса (AppCompatActivity)
        // "super" — ключевое слово, ссылается на родительский класс (AppCompatActivity)
        // "onCreate" — метод родительского класса, который мы вызываем
        // "savedInstanceState" — передаём параметр в родительский метод
        setContentView(R.layout.activity_add_doctor);
        // Устанавливаем макет для активности
        // "setContentView" — метод AppCompatActivity, задаёт макет интерфейса
        // "R.layout.activity_add_doctor" — идентификатор макета, сгенерированный из XML-файла activity_add_doctor.xml
        // "R" — класс, содержащий ресурсы приложения
        // "layout" — подмодуль, содержащий макеты
        // "activity_add_doctor" — имя XML-файла макета

        dbHelper = new HospitalDbHelper(this);
        // Инициализируем dbHelper для работы с базой данных
        // "dbHelper" — наша переменная
        // "new" — ключевое слово, создаёт новый объект
        // "HospitalDbHelper" — класс, объект которого мы создаём
        // "this" — текущий объект (активность), передаётся как контекст для HospitalDbHelper

        firstNameEditText = findViewById(R.id.firstNameEditText);
        // Находим текстовые поля в макете
        // "firstNameEditText" — наша переменная
        // "findViewById" — метод AppCompatActivity, находит элемент интерфейса по идентификатору
        // "R.id.firstNameEditText" — идентификатор текстового поля из XML-макета
        // "R.id" — подмодуль, содержащий идентификаторы элементов
        // "(EditText)" — приведение типа, указывает, что найденный View — это EditText
        lastNameEditText = findViewById(R.id.lastNameEditText);
        // Аналогично для остальных полей
        middleNameEditText = findViewById(R.id.middleNameEditText);
        specializationEditText = findViewById(R.id.specializationEditText);

        specializationEditText.setKeyListener(null);
        // Отключаем прямой ввод текста в поле специальности
        // "specializationEditText" — наше поле
        // "setKeyListener" — метод EditText, задаёт слушатель клавиш (для ввода текста)
        // "null" — передаём null, чтобы отключить ввод текста с клавиатуры

        specializationEditText.setOnTouchListener((v, event) -> {
            // Устанавливаем обработчик касания для поля специальности
            // "specializationEditText" — наше поле
            // "setOnTouchListener" — метод EditText, задаёт обработчик событий касания
            // "(v, event) ->" — лямбда-выражение, определяет, что делать при касании
            // "v" — параметр, View, на который нажали (в данном случае specializationEditText)
            // "event" — параметр, объект MotionEvent, содержит информацию о событии касания
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Проверяем, что это событие нажатия
                // "if" — условный оператор
                // "event.getAction()" — метод MotionEvent, возвращает тип события
                // "==" — оператор сравнения, проверяет равенство
                // "MotionEvent.ACTION_DOWN" — константа, означает событие нажатия (касание экрана)
                showSpecializationDialog();
                // Показываем диалог выбора специальности
                // "showSpecializationDialog" — наш метод, определённый ниже
                return true;
                // Возвращаем true, чтобы указать, что событие обработано
                // "return" — ключевое слово, возвращает значение
                // "true" — булево значение, означает, что событие обработано
            }
            return false;
            // Если событие не обработано, возвращаем false
            // "return" — возвращаем значение
            // "false" — булево значение, означает, что событие не обработано
        });

        Button saveButton = findViewById(R.id.saveButton);
        // Находим кнопку сохранения в макете
        // "Button" — тип переменной, виджет кнопки
        // "saveButton" — имя переменной
        // "findViewById" — находим элемент
        // "R.id.saveButton" — идентификатор кнопки из XML-макета
        // "(Button)" — приведение типа, указывает, что найденный View — это Button
        saveButton.setOnClickListener(v -> saveDoctor());
        // Устанавливаем обработчик нажатия на кнопку
        // "saveButton" — наша кнопка
        // "setOnClickListener" — метод Button, задаёт обработчик нажатия
        // "v ->" — лямбда-выражение, определяет, что делать при нажатии
        // "v" — параметр, View, на который нажали (кнопка)
    }

    private void showSpecializationDialog() {
        // Метод для отображения диалога выбора специальности
        // "private" — модификатор доступа, метод доступен только внутри класса
        // "void" — метод ничего не возвращает
        // "showSpecializationDialog" — имя метода
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Создаём диалог
        // "AlertDialog.Builder" — класс для создания диалога
        // "builder" — имя переменной
        // "new" — создаём новый объект
        // "AlertDialog.Builder" — конструктор класса
        // "this" — текущий объект (активность), передаётся как контекст для диалога
        builder.setTitle("Выберите специальность");
        // Устанавливаем заголовок диалога
        // "builder" — наш объект Builder
        // "setTitle" — метод Builder, задаёт заголовок
        // "Выберите специальность" — строка, текст заголовка

        builder.setItems(specializations, (dialog, which) -> {
            // Устанавливаем список элементов для выбора
            // "setItems" — метод Builder, задаёт список элементов для выбора
            // "specializations" — наш массив специальностей
            // "(dialog, which) ->" — лямбда-выражение, что делать при выборе элемента
            // "dialog" — параметр, объект диалога
            // "which" — параметр, индекс выбранного элемента (начинается с 0)
            specializationEditText.setText(specializations[which]);
            // Устанавливаем выбранную специальность в текстовое поле
            // "specializationEditText" — наше поле
            // "setText" — метод EditText, задаёт текст
            // "specializations" — наш массив
            // "which" — индекс выбранного элемента
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
        // Добавляем кнопку "Отмена"
        // "setNegativeButton" — метод Builder, добавляет кнопку "негативного" действия
        // "Отмена" — текст кнопки
        // "(dialog, which) ->" — лямбда-выражение, что делать при нажатии
        // "dialog" — объект диалога
        // "which" — идентификатор кнопки
        builder.create().show();
        // Создаём и показываем диалог
        // "builder" — наш Builder
        // "create" — метод Builder, создаёт диалог
        // "()" — вызов метода без параметров
        // "show" — метод AlertDialog, показывает диалог
    }

    private void saveDoctor() {
        // Метод для сохранения врача в базу данных
        // "private" — модификатор доступа
        // "void" — метод ничего не возвращает
        // "saveDoctor" — имя метода
        String firstName = firstNameEditText.getText().toString().trim();
        // Получаем текст из текстовых полей
        // "String" — тип переменной, строка
        // "firstName" — имя переменной
        // "firstNameEditText" — наше поле
        // "getText" — метод EditText, возвращает введённый текст как объект Editable
        // "toString" — метод Editable, преобразует в строку
        // "trim" — метод String, удаляет пробелы в начале и конце строки
        String lastName = lastNameEditText.getText().toString().trim();
        // Аналогично для остальных полей
        String middleName = middleNameEditText.getText().toString().trim();
        String specialization = specializationEditText.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || specialization.isEmpty()) {
            // Проверяем, что обязательные поля заполнены
            // "if" — условный оператор
            // "firstName.isEmpty()" — метод String, возвращает true, если строка пустая
            // "||" — логический оператор ИЛИ
            // "lastName.isEmpty()" — проверяем, пуста ли фамилия
            // "specialization.isEmpty()" — проверяем, пуста ли специальность
            Toast.makeText(this, "Заполните обязательные поля: имя, фамилия, специальность", Toast.LENGTH_SHORT).show();
            // Показываем уведомление, если поля не заполнены
            // "Toast" — класс для уведомлений
            // "makeText" — статический метод Toast, создаёт уведомление
            // "this" — контекст (активность)
            // "Заполните обязательные поля: имя, фамилия, специальность" — текст уведомления
            // "Toast.LENGTH_SHORT" — константа, определяет длительность показа (короткая)
            // "show" — метод Toast, показывает уведомление
            return;
            // "return" — прерываем выполнение метода
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Получаем доступ к базе данных для записи
        // "SQLiteDatabase" — тип переменной
        // "db" — имя переменной
        // "dbHelper" — наш объект HospitalDbHelper
        // "getWritableDatabase" — метод HospitalDbHelper, возвращает объект SQLiteDatabase для записи
        ContentValues values = new ContentValues();
        // Создаём объект для хранения данных врача
        // "ContentValues" — тип переменной, объект для хранения пар "ключ-значение"
        // "values" — имя переменной
        // "new" — создаём новый объект
        // "ContentValues" — конструктор класса
        values.put("first_name", firstName);
        // Добавляем данные врача в ContentValues
        // "values" — наш объект ContentValues
        // "put" — метод ContentValues, добавляет пару "ключ-значение"
        // "first_name" — ключ, имя столбца в таблице
        // "firstName" — значение, введённое имя врача
        // Аналогично для остальных полей
        values.put("last_name", lastName);
        values.put("middle_name", middleName);
        values.put("specialization", specialization);

        long newRowId = db.insert("Doctors", null, values);
        // Вставляем данные в таблицу Doctors
        // "long" — тип переменной, длинное целое число
        // "newRowId" — имя переменной, будет хранить идентификатор новой строки
        // "db" — наша база данных
        // "insert" — метод SQLiteDatabase, вставляет данные в таблицу
        // "Doctors" — имя таблицы
        // "null" — параметр, используется, если строка пустая (не используется здесь)
        // "values" — данные для вставки
        if (newRowId != -1) {
            // Проверяем, успешно ли добавлена строка
            // "if" — условный оператор
            // "newRowId" — идентификатор новой строки
            // "!=" — оператор сравнения, не равно
            // "-1" — значение, возвращаемое при ошибке вставки
            Toast.makeText(this, "Врач добавлен", Toast.LENGTH_SHORT).show();
            // Показываем уведомление об успехе
            // "Toast.makeText" — создаём уведомление
            // "this" — контекст
            // "Врач добавлен" — текст уведомления
            // "Toast.LENGTH_SHORT" — длительность
            // "show" — показываем уведомление
            finish();
            // Завершаем активность
            // "finish" — метод AppCompatActivity, закрывает активность
        } else {
            Toast.makeText(this, "Ошибка добавления врача", Toast.LENGTH_SHORT).show();
            // Показываем уведомление об ошибке
        }
        db.close();
    }
}
