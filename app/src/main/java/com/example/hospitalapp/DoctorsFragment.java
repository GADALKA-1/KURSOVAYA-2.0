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
    // "private" — модификатор доступа, делает переменную доступной только внутри класса.
// "HospitalDbHelper" — тип переменной, пользовательский класс для работы с базой данных.
// "dbHelper" — имя переменной, будет использоваться для доступа к базе данных.
    private List<Doctor> doctorList;
    // "private" — модификатор доступа, делает переменную доступной только внутри класса.
// "List" — интерфейс, тип коллекции (список).
// "<Doctor>" — обобщение (generic), указывает, что список будет содержать объекты типа Doctor.
// "doctorList" — имя переменной, будет хранить список врачей.
    private List<Patient> patientList;
    private DoctorAdapter doctorAdapter;
    // "private" — модификатор доступа, делает переменную доступной только внутри класса.
// "DoctorAdapter" — тип переменной, пользовательский класс-адаптер для отображения врачей в списке.
// "doctorAdapter" — имя переменной, будет использоваться для связи списка с данными.
    private PatientAdapter patientAdapter;
    private boolean isListVisible = false;
// "private" — модификатор доступа, делает переменную доступной только внутри класса.
// "boolean" — примитивный тип данных, принимает значения true или false.
// "isListVisible" — имя переменной, будет хранить состояние видимости списка (виден или скрыт).
// "=" — оператор присваивания, присваивает значение справа переменной слева.
// "false" — значение по умолчанию, список изначально скрыт.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // "public" — модификатор доступа, делает метод доступным из других классов.
// "View" — тип возвращаемого значения, метод возвращает объект View (интерфейс фрагмента).
// "onCreateView" — имя метода, вызывается для создания интерфейса фрагмента.
// "LayoutInflater" — тип параметра, объект для создания View из XML.
// "inflater" — имя параметра, будет использоваться для "надувания" макета.
// "ViewGroup" — тип параметра, контейнер, в который добавляется фрагмент.
// "container" — имя параметра, родительский контейнер.
// "Bundle" — тип параметра, объект для хранения данных.
// "savedInstanceState" — имя параметра, содержит сохранённое состояние.
        View view = inflater.inflate(R.layout.fragment_doctors, container, false);
        // "View" — тип переменной, объект интерфейса.
// "view" — имя переменной, будет хранить созданный интерфейс.
// "=" — оператор присваивания.
// "inflater" — объект LayoutInflater, используется для создания View.
// "inflate" — метод класса LayoutInflater, создаёт View из XML.
// "R.layout.fragment_doctor" — идентификатор ресурса макета, где "R" — класс ресурсов, "layout" — тип ресурса, "fragment_doctor" — имя XML-файла.
// "container" — родительский контейнер, передаётся в метод inflate.
// "false" — булево значение, указывает, не прикреплять View к container (это сделает FragmentManager).

        dbHelper = new HospitalDbHelper(getContext());
// "dbHelper" — переменная, в которую сохраняем значение.
// "=" — оператор присваивания.
// "new" — ключевое слово, создаёт новый объект.
// "HospitalDbHelper" — класс, объект которого создаём.
// "getContext" — метод класса Fragment, возвращает контекст (обычно активность).
// "()" — вызов метода без параметров.
        doctorList = new ArrayList<>();
// "doctorList" — переменная, в которую сохраняем значение.
// "=" — оператор присваивания.
// "new" — ключевое слово, создаёт новый объект.
// "ArrayList" — класс, реализация списка.
// "<>" — обобщение, указывает, что список будет содержать объекты Doctor (определяется из типа doctorList).
// "()" — вызов конструктора без параметров.
        patientList = new ArrayList<>();
        doctorAdapter = new DoctorAdapter(getContext(), doctorList);
// "doctorAdapter" — переменная, в которую сохраняем значение.
// "=" — оператор присваивания.
// "new" — ключевое слово, создаёт новый объект.
// "DoctorAdapter" — класс, объект которого создаём.
// "getContext" — метод, возвращает контекст.
// "doctorList" — список врачей, передаём в конструктор адаптера.
        patientAdapter = new PatientAdapter(getContext(), patientList);

        Button addButton = view.findViewById(R.id.addButton);
        Button listDoctorsButton = view.findViewById(R.id.listDoctorsButton);
// "Button" — тип переменной, виджет кнопки.
// "listDoctorsButton" — имя переменной, будет хранить ссылку на кнопку.
// "=" — оператор присваивания.
// "view" — объект View, созданный из макета.
// "findViewById" — метод, находит элемент по идентификатору.
// "R.id.listDoctorsButton" — идентификатор ресурса, где "R" — класс ресурсов, "id" — тип ресурса, "listDoctorsButton" — имя идентификатора.
        Button attachedPatientsButton = view.findViewById(R.id.attachedPatientsButton);
        ListView listView = view.findViewById(R.id.listView);
// "ListView" — тип переменной, виджет списка.
// "listView" — имя переменной, будет хранить ссылку на ListView.
// "=" — оператор присваивания.
// "view" — объект View, созданный из макета.
// "findViewById" — метод, находит элемент по идентификатору.
// "R.id.listView" — идентификатор ресурса, где "R" — класс ресурсов, "id" — тип ресурса, "listView" — имя идентификатора.
        TextView emptyTextView = view.findViewById(R.id.emptyTextView);
// "TextView" — тип переменной, виджет для текста.
// "emptyTextView" — имя переменной, будет хранить ссылку на TextView.
// "=" — оператор присваивания.
// "view" — объект View, созданный из макета.
// "findViewById" — метод, находит элемент по идентификатору.
// "R.id.emptyTextView" — идентификатор ресурса, где "R" — класс ресурсов, "id" — тип ресурса, "emptyTextView" — имя идентификатора.
        listView.setEmptyView(emptyTextView);
// "listView" — объект ListView.
// "setEmptyView" — метод класса ListView, задаёт View, отображаемое, если список пуст.
// "emptyTextView" — объект TextView, который будет показан, если список пуст.

        listView.setVisibility(View.GONE);
// "listView" — объект ListView.
// "setVisibility" — метод класса View, задаёт видимость элемента.
// "View.GONE" — константа класса View, означает, что элемент невидим и не занимает место в макете.

        emptyTextView.setVisibility(View.GONE);
// "emptyTextView" — объект TextView.
// "setVisibility" — метод класса View, задаёт видимость элемента.
// "View.GONE" — константа класса View, означает, что элемент невидим и не занимает место.

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddDoctorActivity.class);
// "Intent" — тип переменной, объект для перехода между активностями.
// "intent" — имя переменной, будет хранить объект Intent.
// "=" — оператор присваивания.
// "new" — ключевое слово, создаёт новый объект.
// "Intent" — класс, объект которого создаём.
// "getActivity" — метод класса Fragment, возвращает активность, в которой находится фрагмент.
// "AddDoctorActivity.class" — класс активности, куда переходим, ".class" — ссылка на класс.

            startActivity(intent);
// "startActivity" — метод класса Fragment, запускает новую активность.
// "intent" — объект Intent, описывает, какую активность запустить.
        });

        listDoctorsButton.setOnClickListener(v -> {
            // "listDoctorsButton" — объект Button
            // Эта строка задаёт обработчик кликов для кнопки "Список врачей"

            if (isListVisible) {
                // "if" — условный оператор
                // "isListVisible" — переменная, хранит состояние видимости списка
                // Условие проверяет, показан ли список

                listView.setVisibility(View.GONE);
                // Скрываем ListView

                emptyTextView.setVisibility(View.GONE);
                // Скрываем emptyTextView

                listDoctorsButton.setText("Список врачей");
                // "setText" — метод, задаёт текст кнопки
                // "Список врачей" — строка, новый текст кнопки
                // Эта строка меняет текст кнопки на "Список врачей"

                isListVisible = false;
                // Устанавливаем флаг видимости в false
            } else {
                // "else" — ветка, выполняется, если условие if ложно
                // Эта ветка выполняется, если список скрыт

                listView.setAdapter(doctorAdapter);
                // "setAdapter" — метод, задаёт адаптер для ListView
                // "doctorAdapter" — параметр, адаптер для списка
                // Эта строка связывает адаптер с ListView для отображения данных

                loadDoctors();
                // "loadDoctors" — метод, определённый ниже
                // "()" — вызов метода без параметров
                // Эта строка вызывает метод для загрузки врачей из базы данных

                listView.setVisibility(View.VISIBLE);
                // "VISIBLE" — константа, означает, что элемент видим
                // Эта строка делает ListView видимым

                listDoctorsButton.setText("Скрыть список");
                // Меняем текст кнопки на "Скрыть список"

                isListVisible = true;
                // Устанавливаем флаг видимости в true
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
        // "private" — модификатор доступа
        // "void" — метод ничего не возвращает
        // "loadDoctors" — имя метода
        // Этот метод загружает врачей из базы данных

        doctorList.clear();
        // "doctorList" — список врачей
        // "clear" — метод, очищает список
        // Эта строка очищает список перед загрузкой новых данных

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // "SQLiteDatabase" — тип переменной, объект базы данных
        // "db" — имя переменной
        // "dbHelper" — объект HospitalDbHelper
        // "getReadableDatabase" — метод, возвращает базу данных в режиме чтения
        // Эта строка открывает базу данных для чтения

        Cursor cursor = db.query("Doctors", null, null, null, null, null, null);
        // "Cursor" — тип переменной, объект для чтения результатов запроса
        // "cursor" — имя переменной
        // "db" — объект базы данных
        // "query" — метод, выполняет SQL-запрос
        // "Doctors" — строка, имя таблицы
        // "null" — параметры (столбцы, условие WHERE, сортировка и т.д.), null означает "выбрать всё"
        // Эта строка выполняет запрос к таблице Doctors, выбирая всех врачей

        while (cursor.moveToNext()) {
            // "while" — цикл, выполняется, пока есть данные
            // "cursor" — объект Cursor
            // "moveToNext" — метод, переходит к следующей строке результата
            // "()" — вызов метода
            // Цикл проходит по всем строкам результата запроса

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
            // "int" — тип переменной
            // "id" — имя переменной
            // "cursor" — объект Cursor
            // "getInt" — метод, извлекает целое число
            // "getColumnIndexOrThrow" — метод, возвращает индекс столбца
            // "doctor_id" — имя столбца
            // Эта строка извлекает doctor_id из текущей строки

            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
            // "String" — тип переменной
            // "firstName" — имя переменной
            // "getString" — метод, извлекает строку
            // "first_name" — имя столбца
            // Эта строка извлекает имя врача

            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            // "lastName" — имя переменной
            // "last_name" — имя столбца
            // Эта строка извлекает фамилию врача

            String middleName = cursor.getString(cursor.getColumnIndexOrThrow("middle_name"));
            // "middleName" — имя переменной
            // "middle_name" — имя столбца
            // Эта строка извлекает отчество врача

            String specialization = cursor.getString(cursor.getColumnIndexOrThrow("specialization"));
            // "specialization" — имя переменной
            // "specialization" — имя столбца
            // Эта строка извлекает специальность врача

            doctorList.add(new Doctor(id, firstName, lastName, middleName, specialization));
            // "doctorList" — список врачей
            // "add" — метод, добавляет элемент в список
            // "new" — создаёт новый объект
            // "Doctor" — класс, конструктор которого вызываем
            // "id, firstName, ..." — параметры конструктора
            // Эта строка создаёт объект Doctor и добавляет его в список
        }
        cursor.close();
        // "cursor" — объект Cursor
        // "close" — метод, закрывает курсор
        // Эта строка закрывает курсор, освобождая ресурсы

        doctorAdapter.notifyDataSetChanged();
        // "doctorAdapter" — адаптер
        // "notifyDataSetChanged" — метод, уведомляет ListView об изменении данных
        // Эта строка обновляет отображение списка
    }

    private void showAttachedPatientsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // "AlertDialog" — класс для создания диалогов
        // "Builder" — вложенный класс для построения диалога
        // "builder" — имя переменной
        // "new" — создаёт новый объект
        // "getContext" — метод, возвращает контекст
        // Эта строка создаёт объект для построения диалога
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
            String middleName = cursor.getString(cursor.getColumnIndexOrThrow("middle_name"));
            String dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow("date_of_birth"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            int docId = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
            String diagnosis = cursor.getString(cursor.getColumnIndexOrThrow("diagnosis"));
            String treatment = cursor.getString(cursor.getColumnIndexOrThrow("treatment"));
            String medications = cursor.getString(cursor.getColumnIndexOrThrow("medications"));
            String ward = cursor.getString(cursor.getColumnIndexOrThrow("ward"));
            String admissionDate = cursor.getString(cursor.getColumnIndexOrThrow("admission_date"));
            int admissionCount = cursor.getInt(cursor.getColumnIndexOrThrow("admission_count"));
            patientList.add(new Patient(id, firstName, lastName, middleName, dateOfBirth, phone, email, address, docId, diagnosis, treatment, medications, ward, admissionDate, admissionCount));
        }
        cursor.close();
        patientAdapter.notifyDataSetChanged();
    }

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
