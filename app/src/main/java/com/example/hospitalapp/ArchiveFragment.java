package com.example.hospitalapp;

import android.app.Activity;
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
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class ArchiveFragment extends Fragment {
// "public" — модификатор доступа, класс доступен из любого места
// "class" — ключевое слово для объявления класса
// "ArchiveFragment" — имя класса (название фрагмента для отображения архива пациентов)
// "extends" — ключевое слово для наследования
// "Fragment" — родительский класс, от которого наследуется этот класс
// Этот класс представляет фрагмент для отображения архива пациентов

    // Объявление приватных полей класса
    private HospitalDbHelper dbHelper;
    // "private" — модификатор доступа, поле доступно только внутри класса
    // "HospitalDbHelper" — тип данных, класс для работы с базой данных (наш собственный класс)
    // "dbHelper" — имя переменной, будет хранить объект для взаимодействия с базой данных
    // Точка с запятой (;) — завершает строку кода

    private List<Patient> patientList;
    // "List" — тип данных, интерфейс для списка
    // "<Patient>" — обобщение (generic), указывает, что список будет содержать объекты типа Patient
    // "patientList" — имя переменной, будет хранить список пациентов из архива

    private PatientAdapter patientAdapter;
    // "PatientAdapter" — тип данных, наш собственный класс-адаптер для отображения пациентов в списке
    // "patientAdapter" — имя переменной, будет хранить адаптер для ListView

    private boolean isListVisible = false;
    // "boolean" — тип данных, логическое значение (true/false)
    // "isListVisible" — имя переменной, указывает, виден ли список
    // "=" — оператор присваивания
    // "false" — начальное значение (список изначально скрыт)

    // Объявление объекта для обработки результатов активности

    private final ActivityResultLauncher<Intent> editPatientLauncher = registerForActivityResult(
            // "final" — модификатор, указывает, что переменная не может быть изменена после инициализации
            // "ActivityResultLauncher" — тип данных, объект для запуска активностей и получения результатов
            // "<Intent>" — обобщение, указывает, что лаунчер работает с Intent
            // "editPatientLauncher" — имя переменной
            // "registerForActivityResult" — метод фрагмента, регистрирует обработчик результатов

            new ActivityResultContracts.StartActivityForResult(),
            // "new" — создаёт новый объект
            // "ActivityResultContracts" — класс с контрактами
            // "StartActivityForResult" — контракт для запуска активности и получения результата
            // Этот параметр задаёт тип контракта

            result -> {
                // "result" — параметр, объект с результатом активности
                // "->" — лямбда-выражение, определяет обработчик результата
                // "{" — начало тела лямбда-выражения

                if (result.getResultCode() == Activity.RESULT_OK) {
                    // "if" — условный оператор
                    // "result" — объект результата
                    // "getResultCode" — метод, возвращает код результата
                    // "==" — оператор сравнения
                    // "Activity" — класс
                    // "RESULT_OK" — константа, означает успешное выполнение
                    // Условие проверяет, успешно ли завершилась активность

                    loadArchive();
                    // "loadArchive" — метод, определённый ниже
                    // "()" — вызов метода без параметров
                    // Эта строка обновляет список архива после успешного восстановления пациента
                }
                // "}" — конец блока if
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // "public" — модификатор доступа
        // "View" — тип возвращаемого значения, метод возвращает объект View (интерфейс фрагмента)
        // "onCreateView" — имя метода, вызывается для создания интерфейса фрагмента
        // "LayoutInflater" — тип параметра, объект для преобразования XML в View
        // "inflater" — имя параметра
        // "ViewGroup" — тип параметра, контейнер для фрагмента
        // "container" — имя параметра
        // "Bundle" — тип параметра
        // "savedInstanceState" — имя параметра, сохранённое состояние

        View view = inflater.inflate(R.layout.fragment_archive, container, false);
        // "View" — тип переменной, объект интерфейса
        // "view" — имя переменной
        // "inflater" — объект LayoutInflater
        // "inflate" — метод, преобразует XML-макет в View
        // "R" — класс, содержащий ресурсы приложения
        // "layout" — подмодуль ресурсов, содержит макеты
        // "fragment_archive" — имя XML-файла макета (res/layout/fragment_archive.xml)
        // "container" — параметр, родительский контейнер
        // "false" — параметр, указывает, что View не нужно сразу добавлять в container
        // Эта строка создаёт интерфейс фрагмента из XML-макета

        dbHelper = new HospitalDbHelper(getContext());
        // "dbHelper" — переменная, в которую записываем объект
        // "new" — ключевое слово, создаёт новый объект
        // "HospitalDbHelper" — класс, конструктор которого вызываем
        // "getContext" — метод фрагмента, возвращает контекст приложения
        // "()" — вызов метода без параметров
        // Эта строка создаёт объект HospitalDbHelper для работы с базой данных

        patientList = new ArrayList<>();
        // "patientList" — переменная, в которую записываем список
        // "new" — создаёт новый объект
        // "ArrayList" — класс, реализующий список
        // "<>" — обобщение (diamond operator), указывает, что список будет содержать объекты Patient
        // Эта строка создаёт пустой список для хранения пациентов

        patientAdapter = new PatientAdapter(getContext(), patientList);
        // "patientAdapter" — переменная, в которую записываем адаптер
        // "PatientAdapter" — класс, конструктор которого вызываем
        // "getContext" — метод, возвращает контекст
        // "patientList" — параметр, список пациентов для адаптера
        // Эта строка создаёт адаптер для отображения пациентов в ListView

        Button listArchiveButton = view.findViewById(R.id.listArchiveButton);
        // "Button" — тип переменной, виджет кнопки
        // "listArchiveButton" — имя переменной
        // "view" — объект View, созданный из макета
        // "findViewById" — метод, ищет элемент в макете по ID
        // "R" — класс ресурсов
        // "id" — подмодуль, содержит ID элементов
        // "listArchiveButton" — ID элемента в XML (android:id="@+id/listArchiveButton")
        // Эта строка находит кнопку в макете

        ListView listView = view.findViewById(R.id.listView);
        // "ListView" — тип переменной, виджет списка
        // "listView" — имя переменной
        // "R.id.listView" — ID элемента в XML (android:id="@+id/listView")
        // Эта строка находит ListView в макете

        TextView emptyTextView = view.findViewById(R.id.emptyTextView);
        // "TextView" — тип переменной, виджет текста
        // "emptyTextView" — имя переменной
        // "R.id.emptyTextView" — ID элемента в XML (android:id="@+id/emptyTextView")
        // Эта строка находит TextView, который будет показан, если список пуст

        listView.setEmptyView(emptyTextView);
        // "listView" — объект ListView
        // "setEmptyView" — метод, задаёт View, отображаемое при пустом списке
        // "emptyTextView" — параметр, TextView для отображения
        // Эта строка связывает emptyTextView с listView, чтобы показывать его, если список пуст

        listView.setVisibility(View.GONE);
        // "setVisibility" — метод, задаёт видимость элемента
        // "View" — класс
        // "GONE" — константа, означает, что элемент невидим и не занимает место
        // Эта строка скрывает ListView при запуске

        emptyTextView.setVisibility(View.GONE);

        listArchiveButton.setOnClickListener(v -> {
            // "setOnClickListener" — метод, задаёт обработчик кликов по кнопке
            // "v" — параметр, объект View (кнопка, по которой кликнули)
            // "->" — лямбда-выражение, определяет обработчик
            // "{" — начало тела лямбда-выражения

            if (isListVisible) {
                // "if" — условный оператор
                // "isListVisible" — переменная, указывает, виден ли список
                // Условие проверяет, виден ли список

                listView.setVisibility(View.GONE);
                // Скрываем ListView

                emptyTextView.setVisibility(View.GONE);
                // Скрываем emptyTextView

                listArchiveButton.setText("Список архива");
                // "setText" — метод, задаёт текст кнопки
                // "Список архива" — строка, новый текст кнопки
                // Эта строка меняет текст кнопки на "Список архива"

                isListVisible = false;
                // Устанавливаем флаг видимости списка в false
            } else {
                // "else" — ветка, выполняется, если условие if ложно
                // Эта ветка выполняется, если список не виден

                listView.setAdapter(patientAdapter);
                // "setAdapter" — метод, задаёт адаптер для ListView
                // "patientAdapter" — параметр, адаптер для списка
                // Эта строка связывает адаптер с ListView

                loadArchive();
                // Загружаем данные архива

                listView.setVisibility(View.VISIBLE);
                // "VISIBLE" — константа, означает, что элемент виден
                // Эта строка делает ListView видимым

                listArchiveButton.setText("Скрыть список");
                // Меняем текст кнопки на "Скрыть список"

                isListVisible = true;
                // Устанавливаем флаг видимости списка в true
            }
            // "}" — конец блока if-else
        });

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // "setOnItemClickListener" — метод, задаёт обработчик кликов по элементам списка
            // "parent" — параметр, родительский View (AdapterView)
            // "view1" — параметр, View элемента, по которому кликнули
            // "position" — параметр, позиция элемента в списке (int)
            // "id" — параметр, ID элемента (long)
            // "->" — лямбда-выражение
            // "{" — начало тела лямбда-выражения

            if (position < patientList.size()) {
                // "position" — позиция элемента
                // "<" — оператор "меньше"
                // "patientList" — список пациентов
                // "size" — метод, возвращает размер списка
                // Условие проверяет, что позиция не превышает размер списка

                Patient patient = patientList.get(position);
                // "Patient" — тип переменной, объект пациента
                // "patient" — имя переменной
                // "patientList" — список пациентов
                // "get" — метод, возвращает элемент по индексу
                // "position" — индекс элемента
                // Эта строка получает пациента из списка по позиции

                showPatientDetailsDialog(patient);
                // "showPatientDetailsDialog" — метод, определённый ниже
                // "patient" — параметр, объект пациента
                // Эта строка вызывает метод для отображения диалога с информацией о пациенте
            }
            // "}" — конец блока if
        });

        return view;
    }

    private void loadArchive() {
        // "private" — модификатор доступа
        // "void" — метод ничего не возвращает
        // "loadArchive" — имя метода
        // Этот метод загружает пациентов из таблицы Archive

        patientList.clear();
        // "patientList" — список пациентов
        // "clear" — метод, очищает список
        // Эта строка очищает список перед загрузкой новых данных

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // "SQLiteDatabase" — тип переменной, объект базы данных
        // "db" — имя переменной
        // "dbHelper" — объект HospitalDbHelper
        // "getReadableDatabase" — метод, возвращает базу данных в режиме чтения
        // Эта строка открывает базу данных для чтения

        Cursor cursor = db.query("Archive", null, null, null, null, null, null);
        // "Cursor" — тип переменной, объект для чтения результатов запроса
        // "cursor" — имя переменной
        // "db" — объект базы данных
        // "query" — метод, выполняет SQL-запрос
        // "Archive" — строка, имя таблицы
        // "null" — параметры (столбцы, условие, сортировка и т.д.), null означает "выбрать всё"
        // Эта строка выполняет запрос к таблице Archive, выбирая всех пациентов

        while (cursor.moveToNext()) {
            // "while" — цикл, выполняется, пока есть данные
            // "cursor" — объект Cursor
            // "moveToNext" — метод, переходит к следующей строке результата
            // "()" — вызов метода
            // Цикл проходит по всем строкам результата запроса

            int archiveId = cursor.getInt(cursor.getColumnIndexOrThrow("archive_id"));
            // "int" — тип переменной
            // "archiveId" — имя переменной
            // "cursor" — объект Cursor
            // "getInt" — метод, извлекает целое число
            // "getColumnIndexOrThrow" — метод, возвращает индекс столбца
            // "archive_id" — имя столбца
            // Эта строка извлекает archive_id из текущей строки

            int patientId = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));
            // "patientId" — имя переменной
            // "patient_id" — имя столбца
            // Эта строка извлекает patient_id из текущей строки

            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
            // "String" — тип переменной
            // "firstName" — имя переменной
            // "getString" — метод, извлекает строку
            // "first_name" — имя столбца
            // Эта строка извлекает имя пациента

            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            // "lastName" — имя переменной
            // "last_name" — имя столбца
            // Эта строка извлекает фамилию пациента

            String middleName = cursor.getString(cursor.getColumnIndexOrThrow("middle_name"));
            // "middleName" — имя переменной
            // "middle_name" — имя столбца
            // Эта строка извлекает отчество пациента

            String dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow("date_of_birth"));
            // "dateOfBirth" — имя переменной
            // "date_of_birth" — имя столбца
            // Эта строка извлекает дату рождения

            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            // "phone" — имя переменной
            // "phone" — имя столбца
            // Эта строка извлекает телефон

            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            // "email" — имя переменной
            // "email" — имя столбца
            // Эта строка извлекает email

            String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            // "address" — имя переменной
            // "address" — имя столбца
            // Эта строка извлекает адрес

            int doctorId = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
            // "doctorId" — имя переменной
            // "doctor_id" — имя столбца
            // Эта строка извлекает ID врача

            String diagnosis = cursor.getString(cursor.getColumnIndexOrThrow("diagnosis"));
            // "diagnosis" — имя переменной
            // "diagnosis" — имя столбца
            // Эта строка извлекает диагноз

            String treatment = cursor.getString(cursor.getColumnIndexOrThrow("treatment"));
            // "treatment" — имя переменной
            // "treatment" — имя столбца
            // Эта строка извлекает метод лечения

            String medications = cursor.getString(cursor.getColumnIndexOrThrow("medications"));
            // "medications" — имя переменной
            // "medications" — имя столбца
            // Эта строка извлекает лекарства

            String ward = cursor.getString(cursor.getColumnIndexOrThrow("ward"));
            // "ward" — имя переменной
            // "ward" — имя столбца
            // Эта строка извлекает палату

            String admissionDate = cursor.getString(cursor.getColumnIndexOrThrow("admission_date"));
            // "admissionDate" — имя переменной
            // "admission_date" — имя столбца
            // Эта строка извлекает дату поступления

            int admissionCount = cursor.getInt(cursor.getColumnIndexOrThrow("admission_count"));
            // "admissionCount" — имя переменной
            // "admission_count" — имя столбца
            // Эта строка извлекает количество поступлений

            patientList.add(new Patient(archiveId, firstName, lastName, middleName, dateOfBirth, phone, email, address, doctorId, diagnosis, treatment, medications, ward, admissionDate, admissionCount));
            // "patientList" — список пациентов
            // "add" — метод, добавляет элемент в список
            // "new" — создаёт новый объект
            // "Patient" — класс, конструктор которого вызываем
            // "archiveId, firstName, ..." — параметры конструктора
            // Эта строка создаёт объект Patient и добавляет его в список
        }
        // "}" — конец цикла while

        cursor.close();
        // "cursor" — объект Cursor
        // "close" — метод, закрывает курсор
        // Эта строка закрывает курсор, освобождая ресурсы

        patientAdapter.notifyDataSetChanged();
        // "patientAdapter" — адаптер
        // "notifyDataSetChanged" — метод, уведомляет ListView об изменении данных
        // Эта строка обновляет отображение списка
    }

    private void showPatientDetailsDialog(Patient patient) {
        // "Patient" — тип параметра
        // "patient" — имя параметра
        // Этот метод показывает диалог с данными пациента

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // "AlertDialog" — класс для создания диалогов
        // "Builder" — вложенный класс для построения диалога
        // "builder" — имя переменной
        // "new" — создаёт новый объект
        // "getContext" — метод, возвращает контекст
        // Эта строка создаёт объект для построения диалога

        builder.setTitle("Информация о пациенте");
        // "builder" — объект Builder
        // "setTitle" — метод, задаёт заголовок диалога
        // "Информация о пациенте" — строка, текст заголовка
        // Эта строка задаёт заголовок диалога

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
                "Количество поступлений: " + patient.getAdmissionCount();
        // "String" — тип переменной
        // "details" — имя переменной
        // "+" — оператор конкатенации строк
        // "\n" — символ новой строки
        // "getFirstName" и другие — методы объекта Patient для получения данных
        // Эта строка формирует текст с информацией о пациенте

        builder.setMessage(details);

        builder.setPositiveButton("Восстановить", (dialog, which) -> {
            // "setPositiveButton" — метод, задаёт кнопку "Восстановить"
            // "Восстановить" — текст кнопки
            // "dialog" — параметр, объект диалога
            // "which" — параметр, ID кнопки
            // "->" — лямбда-выражение
            // "{" — начало тела лямбда-выражения

            Intent intent = new Intent(getContext(), EditPatientActivity.class);
            // "Intent" — тип переменной, объект для запуска активности
            // "intent" — имя переменной
            // "new" — создаёт новый объект
            // "getContext" — метод, возвращает контекст
            // "EditPatientActivity" — класс активности
            // "class" — ключевое слово, указывает класс
            // Эта строка создаёт Intent для запуска EditPatientActivity

            intent.putExtra("patient_id", patient.getId());
            // "putExtra" — метод, добавляет данные в Intent
            // "patient_id" — строка, ключ
            // "patient" — объект Patient
            // "getId" — метод, возвращает ID пациента
            // Эта строка добавляет ID пациента в Intent

            intent.putExtra("first_name", patient.getFirstName());
            // "first_name" — ключ
            // "getFirstName" — метод, возвращает имя пациента
            // Эта строка добавляет имя пациента в Intent

            intent.putExtra("last_name", patient.getLastName());
            // "last_name" — ключ
            // "getLastName" — метод, возвращает фамилию пациента
            // Эта строка добавляет фамилию пациента в Intent

            intent.putExtra("middle_name", patient.getMiddleName());
            // "middle_name" — ключ
            // "getMiddleName" — метод, возвращает отчество пациента
            // Эта строка добавляет отчество пациента в Intent

            intent.putExtra("date_of_birth", patient.getDateOfBirth());
            // "date_of_birth" — ключ
            // "getDateOfBirth" — метод, возвращает дату рождения
            // Эта строка добавляет дату рождения в Intent

            intent.putExtra("phone", patient.getPhone());
            // "phone" — ключ
            // "getPhone" — метод, возвращает телефон
            // Эта строка добавляет телефон в Intent

            intent.putExtra("email", patient.getEmail());
            // "email" — ключ
            // "getEmail" — метод, возвращает email
            // Эта строка добавляет email в Intent

            intent.putExtra("address", patient.getAddress());
            // "address" — ключ
            // "getAddress" — метод, возвращает адрес
            // Эта строка добавляет адрес в Intent

            intent.putExtra("doctor_id", patient.getDoctorId());
            // "doctor_id" — ключ
            // "getDoctorId" — метод, возвращает ID врача
            // Эта строка добавляет ID врача в Intent

            intent.putExtra("diagnosis", patient.getDiagnosis());
            // "diagnosis" — ключ
            // "getDiagnosis" — метод, возвращает диагноз
            // Эта строка добавляет диагноз в Intent

            intent.putExtra("treatment", patient.getTreatment());
            // "treatment" — ключ
            // "getTreatment" — метод, возвращает метод лечения
            // Эта строка добавляет метод лечения в Intent

            intent.putExtra("medications", patient.getMedications());
            // "medications" — ключ
            // "getMedications" — метод, возвращает лекарства
            // Эта строка добавляет лекарства в Intent

            intent.putExtra("ward", patient.getWard());
            // "ward" — ключ
            // "getWard" — метод, возвращает палату
            // Эта строка добавляет палату в Intent

            intent.putExtra("admission_date", patient.getAdmissionDate());
            // "admission_date" — ключ
            // "getAdmissionDate" — метод, возвращает дату поступления
            // Эта строка добавляет дату поступления в Intent

            intent.putExtra("admission_count", patient.getAdmissionCount());
            // "admission_count" — ключ
            // "getAdmissionCount" — метод, возвращает количество поступлений
            // Эта строка добавляет количество поступлений в Intent

            editPatientLauncher.launch(intent);
            // "editPatientLauncher" — объект ActivityResultLauncher
            // "launch" — метод, запускает активность
            // "intent" — параметр, Intent для запуска
            // Эта строка запускает EditPatientActivity с переданными данными
        });

        builder.setNeutralButton("Просмотр истории", (dialog, which) -> {
            // "setNeutralButton" — метод, задаёт нейтральную кнопку "Просмотр истории"
            // "Просмотр истории" — текст кнопки
            // Эта строка добавляет кнопку "Просмотр истории"

            showPatientHistory(patient);
            // "showPatientHistory" — метод, определённый ниже
            // "patient" — параметр, объект пациента
            // Эта строка вызывает метод для отображения истории изменений пациента
        });
        // "}" — конец лямбда-выражения
        // ")" — конец параметров setNeutralButton
        // ";" — конец строки

        builder.setNegativeButton("Закрыть", (dialog, which) -> dialog.dismiss());
        // "setNegativeButton" — метод, задаёт кнопку "Закрыть"
        // "Закрыть" — текст кнопки
        // "dismiss" — метод, закрывает диалог
        // Эта строка добавляет кнопку "Закрыть" и задаёт её действие

        builder.create().show();
        // "create" — метод, создаёт диалог
        // "show" — метод, отображает диалог
        // Эта строка создаёт и показывает диалог
    }

    private void showPatientHistory(Patient patient) {
        // "Patient" — тип параметра
        // "patient" — имя параметра
        // Этот метод показывает диалог с историей изменений пациента

        List<String> historyEntries = new ArrayList<>();
        // "List" — тип данных, интерфейс для списка
        // "<String>" — обобщение, список будет содержать строки
        // "historyEntries" — имя переменной
        // "new" — создаёт новый объект
        // "ArrayList" — класс, реализующий список
        // Эта строка создаёт список для хранения записей истории

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Открываем базу данных для чтения

        int patientId = -1;
        // "int" — тип переменной
        // "patientId" — имя переменной
        // "-1" — начальное значение (значение по умолчанию, если ID не найден)
        // Эта строка создаёт переменную для хранения patient_id

        Cursor cursor = db.query("Archive", new String[]{"patient_id"}, "archive_id = ?", new String[]{String.valueOf(patient.getId())}, null, null, null);
        // "new" — создаёт новый объект
        // "String" — класс для работы со строками
        // "[]" — объявление массива
        // "{}" — инициализация массива
        // "patient_id" — строка, имя столбца, который выбираем
        // "archive_id = ?" — условие WHERE
        // "String.valueOf" — метод, преобразует ID в строку
        // "patient" — объект Patient
        // "getId" — метод, возвращает ID (в данном случае archive_id)
        // Эта строка выполняет запрос к таблице Archive, чтобы получить patient_id

        if (cursor.moveToFirst()) {
            // "moveToFirst" — метод, переходит к первой строке результата
            // Условие проверяет, есть ли данные

            patientId = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));
            // Извлекаем patient_id из результата запроса
        }
        // "}" — конец блока if

        cursor.close();

        if (patientId == -1) {
            // "==" — оператор сравнения
            // "-1" — значение, означающее, что ID не найден
            // Условие проверяет, удалось ли найти patient_id

            Toast.makeText(getContext(), "Не удалось найти patient_id", Toast.LENGTH_SHORT).show();
            // "Toast" — класс для уведомлений
            // "makeText" — метод, создаёт уведомление
            // "getContext" — метод, возвращает контекст
            // "Не удалось найти patient_id" — текст уведомления
            // "LENGTH_SHORT" — константа, длительность показа (короткая)
            // "show" — метод, отображает уведомление
            // Эта строка показывает уведомление об ошибке

            return;
            // "return" — ключевое слово, завершает выполнение метода
            // Эта строка завершает метод, если patient_id не найден
        }

        cursor = db.query("PatientHistory", null, "patient_id = ?", new String[]{String.valueOf(patientId)}, null, null, "update_date ASC");
        // "PatientHistory" — имя таблицы
        // "patient_id = ?" — условие WHERE
        // "update_date ASC" — сортировка по дате обновления (по возрастанию)
        // Эта строка выполняет запрос к таблице PatientHistory для получения истории изменений

        while (cursor.moveToNext()) {
            // Цикл проходит по всем строкам результата запроса

            String diagnosis = cursor.getString(cursor.getColumnIndexOrThrow("diagnosis"));
            // Извлекаем диагноз из текущей строки

            String treatment = cursor.getString(cursor.getColumnIndexOrThrow("treatment"));
            // Извлекаем метод лечения

            String medications = cursor.getString(cursor.getColumnIndexOrThrow("medications"));
            // Извлекаем лекарства

            String ward = cursor.getString(cursor.getColumnIndexOrThrow("ward"));
            // Извлекаем палату

            String updateDate = cursor.getString(cursor.getColumnIndexOrThrow("update_date"));
            // "updateDate" — имя переменной
            // "update_date" — имя столбца
            // Эта строка извлекает дату обновления

            String entry = "Дата обновления: " + updateDate + "\n" +
                    "Диагноз: " + diagnosis + "\n" +
                    "Лечение: " + treatment + "\n" +
                    "Лекарства: " + medications + "\n" +
                    "Палата: " + ward + "\n" +
                    "-------------------";
            // "entry" — имя переменной
            // Формируем строку с записью истории

            historyEntries.add(entry);
            // "historyEntries" — список записей
            // "add" — метод, добавляет элемент в список
            // "entry" — строка с записью
            // Эта строка добавляет запись в список
        }
        // "}" — конец цикла while

        cursor.close();
        // Закрываем курсор

        db.close();

        if (historyEntries.isEmpty()) {
            // "isEmpty" — метод, проверяет, пуст ли список
            // Условие проверяет, есть ли записи в истории

            Toast.makeText(getContext(), "История изменений пуста", Toast.LENGTH_SHORT).show();
            // Показываем уведомление, если история пуста

            return;
            // Завершаем метод
        }
        // "}" — конец блока if

        AlertDialog.Builder historyBuilder = new AlertDialog.Builder(getContext());
        // "historyBuilder" — имя переменной
        // Создаём объект для построения диалога истории

        historyBuilder.setTitle("История изменений пациента");
        // Задаём заголовок диалога

        StringBuilder historyText = new StringBuilder();
        // "StringBuilder" — класс для построения строк
        // "historyText" — имя переменной
        // Эта строка создаёт объект для формирования текста истории

        for (String entry : historyEntries) {
            // "for" — цикл для перебора элементов
            // "String" — тип переменной
            // "entry" — имя переменной цикла
            // ":" — оператор для перебора
            // "historyEntries" — список записей
            // Цикл проходит по всем записям в списке

            historyText.append(entry).append("\n");
            // "append" — метод, добавляет текст
            // "entry" — строка с записью
            // "\n" — новая строка
            // Эта строка добавляет запись в текст с новой строкой
        }
        // "}" — конец цикла for

        historyBuilder.setMessage(historyText.toString());
        // "toString" — метод, преобразует StringBuilder в строку
        // "setMessage" — метод, задаёт текст сообщения
        // Эта строка задаёт текст диалога

        historyBuilder.setPositiveButton("Закрыть", (dialog, which) -> dialog.dismiss());
        // Добавляем кнопку "Закрыть"

        historyBuilder.create().show();
        // Создаём и показываем диалог
    }
    // "}" — конец метода showPatientHistory

    // Переопределение метода onResume
    @Override
    public void onResume() {
        // "onResume" — метод, вызывается при возобновлении фрагмента

        super.onResume();
        // "super" — ключевое слово, обращается к родительскому классу
        // "onResume" — вызов метода родительского класса
        // Эта строка вызывает onResume родительского класса

        if (isListVisible) {
            // Проверяем, виден ли список

            loadArchive();
            // Обновляем список архива
        }
        // "}" — конец блока if
    }
    // "}" — конец метода onResume
}
