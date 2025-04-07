// HospitalDbHelper.java
// Этот класс управляет базой данных приложения "HospitalApp".
package com.example.hospitalapp;

import android.content.Context; // Импортируем класс Context для работы с контекстом приложения.
import android.database.sqlite.SQLiteDatabase; // Импортируем класс для работы с базой данных SQLite.
import android.database.sqlite.SQLiteOpenHelper; // Импортируем базовый класс для создания и обновления базы данных.

// Объявляем публичный класс HospitalDbHelper, который наследуется от SQLiteOpenHelper.
public class HospitalDbHelper extends SQLiteOpenHelper {
    // Объявляем приватную статическую константу для имени файла базы данных.
    private static final String DATABASE_NAME = "hospital.db";
    // "hospital.db" — это имя файла, в котором будут храниться все данные приложения.

    // Объявляем приватную статическую константу для версии базы данных.
    private static final int DATABASE_VERSION = 4;
    // Число 4 указывает версию базы данных. При изменении схемы базы данных это число увеличивается.

    // Конструктор класса, который принимает контекст приложения.
    public HospitalDbHelper(Context context) {
        // Вызываем конструктор родительского класса SQLiteOpenHelper.
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // context — это объект, предоставляющий доступ к ресурсам приложения.
        // DATABASE_NAME — имя базы данных, определённое выше.
        // null — указывает, что мы не используем фабрику курсоров (по умолчанию).
        // DATABASE_VERSION — версия базы данных, определённая выше.
    }

    // Метод onCreate вызывается, когда база данных создаётся впервые.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // db — это объект SQLiteDatabase, через который мы выполняем SQL-запросы.

        // Объявляем строку с SQL-запросом для создания таблицы Patients.
        String createPatientsTable = "CREATE TABLE Patients (" +
                "patient_id INTEGER PRIMARY KEY AUTOINCREMENT, " + // Столбец patient_id — уникальный идентификатор пациента, автоматически увеличивается.
                "first_name TEXT, " + // Столбец first_name — имя пациента, тип TEXT (строка).
                "last_name TEXT, " + // Столбец last_name — фамилия пациента, тип TEXT.
                "middle_name TEXT, " + // Столбец middle_name — отчество пациента, тип TEXT.
                "date_of_birth TEXT, " + // Столбец date_of_birth — дата рождения, тип TEXT (например, "2000-01-01").
                "phone TEXT, " + // Столбец phone — номер телефона, тип TEXT.
                "email TEXT, " + // Столбец email — адрес электронной почты, тип TEXT.
                "address TEXT, " + // Столбец address — адрес проживания, тип TEXT.
                "doctor_id INTEGER, " + // Столбец doctor_id — идентификатор врача, тип INTEGER, внешний ключ.
                "diagnosis TEXT, " + // Столбец diagnosis — диагноз пациента, тип TEXT.
                "treatment TEXT, " + // Столбец treatment — назначенное лечение, тип TEXT.
                "medications TEXT, " + // Столбец medications — список лекарств, тип TEXT.
                "ward TEXT, " + // Столбец ward — номер палаты, тип TEXT.
                "admission_date TEXT, " + // Столбец admission_date — дата поступления, тип TEXT.
                "admission_count INTEGER DEFAULT 1, " + // Столбец admission_count — количество поступлений, тип INTEGER, по умолчанию 1.
                "FOREIGN KEY(doctor_id) REFERENCES Doctors(doctor_id))"; // Указываем, что doctor_id ссылается на doctor_id в таблице Doctors.

        // Выполняем SQL-запрос для создания таблицы Patients.
        db.execSQL(createPatientsTable);
        // Метод execSQL выполняет SQL-запрос без возврата результата.

        // Аналогично создаём таблицу Doctors.
        String createDoctorsTable = "CREATE TABLE Doctors (" +
                "doctor_id INTEGER PRIMARY KEY AUTOINCREMENT, " + // Уникальный идентификатор врача.
                "first_name TEXT, " + // Имя врача.
                "last_name TEXT, " + // Фамилия врача.
                "middle_name TEXT, " + // Отчество врача.
                "specialization TEXT)"; // Специализация врача.
        db.execSQL(createDoctorsTable);

        // Создаём таблицу Archive для хранения данных выписанных пациентов.
        String createArchiveTable = "CREATE TABLE Archive (" +
                "archive_id INTEGER PRIMARY KEY AUTOINCREMENT, " + // Уникальный идентификатор записи в архиве.
                "patient_id INTEGER, " + // ID пациента из таблицы Patients.
                "first_name TEXT, " + // Имя пациента.
                "last_name TEXT, " + // Фамилия пациента.
                "middle_name TEXT, " + // Отчество пациента.
                "date_of_birth TEXT, " + // Дата рождения.
                "phone TEXT, " + // Телефон.
                "email TEXT, " + // Email.
                "address TEXT, " + // Адрес.
                "doctor_id INTEGER, " + // ID врача.
                "diagnosis TEXT, " + // Диагноз.
                "treatment TEXT, " + // Лечение.
                "medications TEXT, " + // Лекарства.
                "ward TEXT, " + // Палата.
                "admission_date TEXT, " + // Дата поступления.
                "discharge_date TEXT, " + // Дата выписки.
                "admission_count INTEGER)"; // Количество поступлений.
        db.execSQL(createArchiveTable);

        // Создаём таблицу PatientHistory для истории изменений данных пациентов.
        String createPatientHistoryTable = "CREATE TABLE PatientHistory (" +
                "history_id INTEGER PRIMARY KEY AUTOINCREMENT, " + // Уникальный идентификатор записи в истории.
                "patient_id INTEGER, " + // ID пациента, к которому относится запись.
                "diagnosis TEXT, " + // Диагноз на момент изменения.
                "treatment TEXT, " + // Лечение на момент изменения.
                "medications TEXT, " + // Лекарства на момент изменения.
                "ward TEXT, " + // Палата на момент изменения.
                "update_date TEXT, " + // Дата изменения данных.
                "FOREIGN KEY(patient_id) REFERENCES Patients(patient_id))"; // Внешний ключ на таблицу Patients.
        db.execSQL(createPatientHistoryTable);
    }

    // Метод onUpgrade вызывается, когда версия базы данных увеличивается.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db — объект базы данных.
        // oldVersion — старая версия базы данных.
        // newVersion — новая версия базы данных.

        // Удаляем все таблицы, если версия изменилась.
        db.execSQL("DROP TABLE IF EXISTS Patients"); // Удаляем таблицу Patients, если она существует.
        db.execSQL("DROP TABLE IF EXISTS Doctors"); // Удаляем таблицу Doctors.
        db.execSQL("DROP TABLE IF EXISTS Archive"); // Удаляем таблицу Archive.
        db.execSQL("DROP TABLE IF EXISTS PatientHistory"); // Удаляем таблицу PatientHistory.

        // Создаём таблицы заново с новой схемой.
        onCreate(db);
        // Вызываем метод onCreate, чтобы пересоздать все таблицы.
    }
}
