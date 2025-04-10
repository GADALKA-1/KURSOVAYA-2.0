package com.example.hospitalapp;

import android.app.AlertDialog;
import android.content.ContentValues;
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
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PatientsFragment extends Fragment {
   // "public" — модификатор доступа, делает класс доступным из других классов.
    // "class" — ключевое слово для объявления класса.
    // "AttachedPatientsFragment" — имя класса, отражающее его назначение (фрагмент для отображения прикреплённых пациентов).
    // "extends" — ключевое слово, указывающее, что этот класс наследуется от другого класса.
    // "Fragment" — родительский класс, от которого наследуемся, чтобы использовать функциональность фрагментов.
    private HospitalDbHelper dbHelper;
    // Объявление приватной переменной "dbHelper" типа "HospitalDbHelper".
    // "private" — модификатор доступа, делает переменную доступной только внутри этого класса.
    // "HospitalDbHelper" — пользовательский класс, который мы создали для работы с базой данных SQLite.
    // "dbHelper" — имя переменной, которая будет хранить объект для взаимодействия с базой данных.
    private List<Patient> patientList;
    // Объявление приватной переменной "patientList" типа "List<Patient>".
    // "List" — интерфейс из пакета "java.util", представляющий упорядоченный список объектов.
    // "<Patient>" — параметризация типа (generics), указывает, что список будет содержать объекты типа "Patient".
    // "patientList" — имя переменной, которая будет хранить список пациентов.
    private PatientAdapter patientAdapter;
    // Объявление приватной переменной "patientAdapter" типа "PatientAdapter".
    // "PatientAdapter" — пользовательский класс, который мы создали для адаптации списка пациентов к "ListView".
    // "patientAdapter" — имя переменной, которая будет хранить адаптер для списка.
    private boolean isListVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Переопределение метода "onCreateView" из родительского класса "Fragment".
        // "@Override" — аннотация, указывающая, что метод переопределяет метод родительского класса.
        // "public" — модификатор доступа.
        // "View" — тип возвращаемого значения, метод возвращает объект "View" (макет фрагмента).
        // "onCreateView" — имя метода, вызываемого для создания пользовательского интерфейса фрагмента.
        // "LayoutInflater inflater" — параметр, объект для преобразования XML-макета в "View".
        // "ViewGroup container" — параметр, родительский контейнер, в который будет помещён фрагмент.
        // "Bundle savedInstanceState" — параметр, объект "Bundle" с сохранённым состоянием.
        View view = inflater.inflate(R.layout.fragment_patients, container, false);
        // Создание объекта "View" из XML-макета.
        // "View" — тип переменной, объект, представляющий макет фрагмента.
        // "view" — имя переменной, в которую сохраняем созданный макет.
        // "inflater" — объект "LayoutInflater", переданный как параметр.
        // "inflate" — метод "LayoutInflater", преобразует XML-макет в "View".
        // "R.layout.fragment_attached_patients" — идентификатор ресурса макета, где "R" — автоматически сгенерированный класс, "layout" — подмодуль, "fragment_attached_patients" — имя XML-файла макета.
        // "container" — родительский контейнер, переданный как параметр.
        // "false" — булево значение, указывает, что не нужно сразу добавлять "View" в "container".

        dbHelper = new HospitalDbHelper(getContext());
        // Инициализация объекта "dbHelper".
        // "dbHelper" — переменная, объявленная ранее.
        // "new" — ключевое слово для создания нового объекта.
        // "HospitalDbHelper" — класс-конструктор, создающий объект для работы с базой данных.
        // "getContext()" — метод класса "Fragment", возвращает контекст приложения (нужен для "HospitalDbHelper").
        // "()" — вызов конструктора "HospitalDbHelper".
        patientList = new ArrayList<>();
        // Инициализация списка "patientList".
        // "patientList" — переменная, объявленная ранее.
        // "new" — создание нового объекта.
        // "ArrayList" — класс, реализующий интерфейс "List", используется для хранения списка объектов.
        // "<Patient>" — параметризация типа, указывает, что список будет содержать объекты "Patient".
        // "()" — вызов конструктора "ArrayList".
        patientAdapter = new PatientAdapter(getContext(), patientList);
        // Инициализация адаптера "patientAdapter".
        // "patientAdapter" — переменная, объявленная ранее.
        // "new" — создание нового объекта.
        // "PatientAdapter" — пользовательский класс адаптера.
        // "getContext()" — получение контекста приложения.
        // "patientList" — список пациентов, который передаём в адаптер.
        // "()" — вызов конструктора "PatientAdapter".

        Button addButton = view.findViewById(R.id.addButton);
        // Находим кнопку addButton в макете по её идентификатору
        // view.findViewById — метод для поиска элемента в View по ID
        // R.id.addButton — идентификатор кнопки (определён в fragment_patients.xml)
        // Button — тип виджета (кнопка)
        Button listPatientsButton = view.findViewById(R.id.listPatientsButton);
        // Находим кнопку listPatientsButton в макете по её идентификатору
        // R.id.listPatientsButton — идентификатор кнопки для показа/скрытия списка
        ListView listView = view.findViewById(R.id.listView);
        // Поиск "ListView" в макете.
        // "ListView" — тип переменной, виджет для отображения списка.
        // "listView" — имя переменной, в которую сохраняем найденный "ListView".
        // "view" — объект "View", созданный из макета.
        // "findViewById" — метод класса "View", ищет виджет по идентификатору.
        // "R.id.listView" — идентификатор ресурса, где "R" — класс ресурсов, "id" — подмодуль, "listView" — имя идентификатора в XML.
        TextView emptyTextView = view.findViewById(R.id.emptyTextView);
        // Поиск "TextView" в макете.
        // "TextView" — тип переменной, виджет для отображения текста.
        // "emptyTextView" — имя переменной, в которую сохраняем найденный "TextView".
        // "view" — объект "View".
        // "findViewById" — метод для поиска виджета.
        // "R.id.emptyTextView" — идентификатор ресурса, где "emptyTextView" — имя идентификатора в XML.
        listView.setEmptyView(emptyTextView);
        // Установка "emptyTextView" как пустого представления для "listView".
        // "listView" — объект "ListView".
        // "setEmptyView" — метод "ListView", задаёт виджет, который отображается, если список пуст.
        // "emptyTextView" — объект "TextView", который будет показан, если в списке нет элементов.

        listView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.GONE);

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddPatientActivity.class);
            startActivity(intent);
        });

        listPatientsButton.setOnClickListener(v -> {
            if (isListVisible) {
                listView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.GONE);
                listPatientsButton.setText("Список пациентов");
                isListVisible = false;
            } else {
                listView.setAdapter(patientAdapter);
                loadPatients();
                listView.setVisibility(View.VISIBLE);
                listPatientsButton.setText("Скрыть список");
                isListVisible = true;
            }
        });

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            if (position < patientList.size()) {
                Patient patient = patientList.get(position);
                showPatientDetailsDialog(patient);
            }
        });

        return view;
    }

    private void loadPatients() {
        // Метод для загрузки пациентов, прикреплённых к врачу
        // "private" — модификатор доступа, метод доступен только внутри класса
        // "void" — метод ничего не возвращает
        patientList.clear();
        // Очищаем список пациентов
        // "patientList" — наш список
        // "clear" — метод List, удаляет все элементы из списка
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Получаем доступ к базе данных для чтения
        // "SQLiteDatabase" — тип переменной
        // "db" — имя переменной
        // "dbHelper" — наш объект HospitalDbHelper
        // "getReadableDatabase" — метод HospitalDbHelper, возвращает объект SQLiteDatabase для чтения
        Cursor cursor = db.query("Patients", null, null, null, null, null, null);
        // Выполняем запрос к таблице Patients
        // "Cursor" — тип переменной, объект для работы с результатами запроса
        // "cursor" — имя переменной
        // "db" — наша база данных
        // "query" — метод SQLiteDatabase, выполняет SQL-запрос
        // "Patients" — строка, имя таблицы в базе данных
        // "null" — параметр, массив столбцов (null означает все столбцы)
        // "doctor_id = ?" — строка, условие WHERE для фильтрации (ищем пациентов с заданным doctor_id)
        // "new String[]{String.valueOf(doctorId)}" — массив строк, значения для подстановки в условие WHERE
        // "new String[]" — создаём новый массив строк
        // "String.valueOf" — метод, преобразует doctorId (int) в строку
        // "doctorId" — наш идентификатор врача
        // "null" — параметр, группировка (не используется)
        // "null" — параметр, условие HAVING (не используется)
        // "null" — параметр, сортировка (не используется)
        while (cursor.moveToNext()) {
            // Проходим по всем строкам результата запроса
            // "while" — цикл, выполняется, пока условие истинно
            // "cursor" — наш курсор
            // "moveToNext" — метод Cursor, переходит к следующей строке (возвращает true, если строка есть)
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));
            // Извлекаем данные пациента из курсора
            // "int" — тип переменной
            // "id" — имя переменной
            // "cursor" — наш курсор
            // "getInt" — метод Cursor, извлекает целое число
            // "getColumnIndexOrThrow" — метод Cursor, возвращает индекс столбца по имени
            // "patient_id" — имя столбца в таблице
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            String middleName = cursor.getString(cursor.getColumnIndexOrThrow("middle_name"));
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
            patientList.add(new Patient(id, firstName, lastName, middleName, dateOfBirth, phone, email, address, doctorId, diagnosis, treatment, medications, ward, admissionDate, admissionCount));
        }
        // Добавляем нового пациента в список
        // "patientList" — наш список
        // "add" — метод List, добавляет элемент в список
        // "new Patient" — создаём новый объект Patient
        // Передаём все извлечённые данные в конструктор Patient
        cursor.close();
        // Закрываем курсор
        // "cursor" — наш курсор
        // "close" — метод Cursor, освобождает ресурсы
        patientAdapter.notifyDataSetChanged();
    }
    // Уведомляем адаптер об изменении данных
    // "patientAdapter" — наш адаптер
    // "notifyDataSetChanged" — метод ArrayAdapter, сообщает, что данные изменились, и список нужно обновить

    private void showPatientDetailsDialog(Patient patient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Информация о пациенте");

        String details = "Имя: " + patient.getFirstName() + "\n" +
                "Фамилия: " + patient.getLastName() + "\n" +
                "Отчество: " + patient.getMiddleName() + "\n" +
                "Дата рождения: " + patient.getDateOfBirth() + "\n" +
                "Телефон: " + patient.getPhone() + "\n" +
                "Email: " + patient.getEmail() + "\n" +
                "Адрес: " + patient.getAddress() + "\n" +
                "Диагноз: " + patient.getDiagnosis() + "\n" +
                "Лечение: " + patient.getTreatment() + "\n" +
                "Лекарства: " + patient.getMedications() + "\n" +
                "Палата: " + patient.getWard() + "\n" +
                "Количество поступлений: " + patient.getAdmissionCount(); // Добавляем строку с количеством поступлений
        // Формируем строку с информацией о пациенте
        // "String" — тип переменной
        // "details" — имя переменной
        // "Имя: " — строка, начало текста
        // "patient.getFirstName()" — метод Patient, возвращает имя пациента
        // "\n" — символ новой строки
        builder.setMessage(details);
        // Устанавливаем текст диалога
        // "builder" — наш Builder
        // "setMessage" — метод Builder, задаёт текст сообщения
        // "details" — строка с информацией

        builder.setNeutralButton("Выписать", (dialog, which) -> {
            dischargePatient(patient);
        });

        builder.setPositiveButton("Закрыть", (dialog, which) -> dialog.dismiss());
        // Добавляем кнопку "Закрыть"
        // "setPositiveButton" — метод Builder, добавляет кнопку "позитивного" действия
        // "Закрыть" — текст кнопки
        // "(dialog, which) ->" — лямбда-выражение, что делать при нажатии
        // "dialog" — параметр, объект диалога
        // "which" — параметр, идентификатор кнопки
        builder.create().show();
        // Создаём и показываем диалог
        // "builder.create()" — метод Builder, создаёт диалог
        // "show()" — метод AlertDialog, показывает диалог
    }

    private void dischargePatient(Patient patient) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues archiveValues = new ContentValues();
            archiveValues.put("patient_id", patient.getId());
            archiveValues.put("first_name", patient.getFirstName());
            archiveValues.put("last_name", patient.getLastName());
            archiveValues.put("middle_name", patient.getMiddleName());
            archiveValues.put("date_of_birth", patient.getDateOfBirth());
            archiveValues.put("phone", patient.getPhone());
            archiveValues.put("email", patient.getEmail());
            archiveValues.put("address", patient.getAddress());
            archiveValues.put("doctor_id", patient.getDoctorId());
            archiveValues.put("diagnosis", patient.getDiagnosis());
            archiveValues.put("treatment", patient.getTreatment());
            archiveValues.put("medications", patient.getMedications());
            archiveValues.put("ward", patient.getWard());
            archiveValues.put("admission_date", patient.getAdmissionDate());
            archiveValues.put("discharge_date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
            archiveValues.put("admission_count", patient.getAdmissionCount());

            long archiveRowId = db.insert("Archive", null, archiveValues);
            if (archiveRowId != -1) {
                int deletedRows = db.delete("Patients", "patient_id = ?", new String[]{String.valueOf(patient.getId())});
                if (deletedRows > 0) {
                    Toast.makeText(getContext(), "Пациент выписан", Toast.LENGTH_SHORT).show();
                    loadPatients();
                } else {
                    Toast.makeText(getContext(), "Ошибка при удалении пациента", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Ошибка при добавлении в архив", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }

    @Override
    public void onResume() {
        // Переопределяем метод onResume, вызывается при возобновлении фрагмента
        //    // "@Override" — аннотация
        //    // "public" — модификатор доступа
        //    // "void" — метод ничего не возвращает
        //    // "onResume" — имя метода
        super.onResume();
        // Вызываем onResume родительского класса
        // "super" — родительский класс
        // "onResume" — метод родительского класса
        if (isListVisible) {
            loadPatients();
        }
    }
}
