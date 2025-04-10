package com.example.hospitalapp;

public class Patient {
// "public" — модификатор доступа, класс доступен из любого места в приложении
// "class" — ключевое слово для объявления класса
// "Patient" — имя класса, отражает, что этот класс представляет пациента
// Фигурные скобки {} — начало тела класса
// Этот класс будет использоваться для хранения данных о пациенте

    // Объявление приватных полей класса
    private int id;
    // "private" — модификатор доступа, поле доступно только внутри класса
    // "int" — тип данных, целое число (32-битное, например, 1, 100, -5)
    // "id" — имя переменной, будет хранить уникальный идентификатор пациента
    // Точка с запятой (;) — завершает строку кода
    // Это поле хранит ID пациента (например, patient_id из базы данных)

    private String firstName;
    // "String" — тип данных, класс для работы со строками (например, "Иван")
    // "firstName" — имя переменной, будет хранить имя пациента
    // Это поле хранит имя пациента

    private String lastName;
    // "lastName" — имя переменной, будет хранить фамилию пациента
    // Это поле хранит фамилию пациента

    private String middleName;
    // "middleName" — имя переменной, будет хранить отчество пациента
    // Это поле хранит отчество пациента

    private String dateOfBirth;
    // "dateOfBirth" — имя переменной, будет хранить дату рождения пациента
    // Это поле хранит дату рождения в формате строки (например, "1990-01-01")

    private String phone;
    // "phone" — имя переменной, будет хранить номер телефона пациента
    // Это поле хранит телефон пациента (например, "+79991234567")

    private String email;
    // "email" — имя переменной, будет хранить email пациента
    // Это поле хранит email пациента (например, "patient@example.com")

    private String address;
    // "address" — имя переменной, будет хранить адрес пациента
    // Это поле хранит адрес пациента (например, "ул. Ленина, д. 10")

    private int doctorId;
    // "doctorId" — имя переменной, будет хранить ID врача, к которому прикреплён пациент
    // Это поле хранит ID врача (например, 1, 2, 3)

    private String diagnosis;
    // "diagnosis" — имя переменной, будет хранить диагноз пациента
    // Это поле хранит диагноз (например, "ОРВИ")

    private String treatment;
    // "treatment" — имя переменной, будет хранить метод лечения пациента
    // Это поле хранит метод лечения (например, "Антибиотики")

    private String medications;
    // "medications" — имя переменной, будет хранить список лекарств пациента
    // Это поле хранит лекарства (например, "Парацетамол, Ибупрофен")

    private String ward;
    // "ward" — имя переменной, будет хранить номер палаты пациента
    // Это поле хранит палату (например, "Палата 5")

    private String admissionDate;
    // "admissionDate" — имя переменной, будет хранить дату поступления пациента
    // Это поле хранит дату поступления (например, "2023-10-01")

    private int admissionCount;
    // "admissionCount" — имя переменной, будет хранить количество поступлений пациента
    // Это поле хранит, сколько раз пациент поступал в больницу (например, 1, 2, 3)

    public Patient(int id, String firstName, String lastName, String middleName, String dateOfBirth, String phone, String email,
                   String address, int doctorId, String diagnosis, String treatment, String medications, String ward,
                   String admissionDate, int admissionCount) {
        // "public" — модификатор доступа, конструктор доступен из любого места
        // "Patient" — имя конструктора, совпадает с именем класса
        // Скобки () — содержат параметры конструктора
        // "int id" — параметр, ID пациента
        // "String firstName" — параметр, имя пациента
        // "String lastName" — параметр, фамилия пациента
        // "String middleName" — параметр, отчество пациента
        // "String dateOfBirth" — параметр, дата рождения
        // "String phone" — параметр, телефон
        // "String email" — параметр, email
        // "String address" — параметр, адрес
        // "int doctorId" — параметр, ID врача
        // "String diagnosis" — параметр, диагноз
        // "String treatment" — параметр, метод лечения
        // "String medications" — параметр, лекарства
        // "String ward" — параметр, палата
        // "String admissionDate" — параметр, дата поступления
        // "int admissionCount" — параметр, количество поступлений
        // Фигурные скобки {} — начало тела конструктора
        // Этот конструктор создаёт объект Patient и инициализирует его поля
        this.id = id;
        // "this" — ключевое слово, ссылается на текущий объект класса
        // "id" — поле класса
        // "=" — оператор присваивания
        // "id" — параметр конструктора
        // Эта строка присваивает значение параметра id полю id объекта

        this.firstName = firstName;
        // "firstName" — поле класса
        // "firstName" — параметр конструктора
        // Эта строка присваивает значение параметра firstName полю firstName объекта

        this.lastName = lastName;
        // "lastName" — поле класса
        // "lastName" — параметр конструктора
        // Эта строка присваивает значение параметра lastName полю lastName объекта

        this.middleName = middleName;
        // "middleName" — поле класса
        // "middleName" — параметр конструктора
        // Эта строка присваивает значение параметра middleName полю middleName объекта

        this.dateOfBirth = dateOfBirth;
        // "dateOfBirth" — поле класса
        // "dateOfBirth" — параметр конструктора
        // Эта строка присваивает значение параметра dateOfBirth полю dateOfBirth объекта

        this.phone = phone;
        // "phone" — поле класса
        // "phone" — параметр конструктора
        // Эта строка присваивает значение параметра phone полю phone объекта

        this.email = email;
        // "email" — поле класса
        // "email" — параметр конструктора
        // Эта строка присваивает значение параметра email полю email объекта

        this.address = address;
        // "address" — поле класса
        // "address" — параметр конструктора
        // Эта строка присваивает значение параметра address полю address объекта

        this.doctorId = doctorId;
        // "doctorId" — поле класса
        // "doctorId" — параметр конструктора
        // Эта строка присваивает значение параметра doctorId полю doctorId объекта

        this.diagnosis = diagnosis;
        // "diagnosis" — поле класса
        // "diagnosis" — параметр конструктора
        // Эта строка присваивает значение параметра diagnosis полю diagnosis объекта

        this.treatment = treatment;
        // "treatment" — поле класса
        // "treatment" — параметр конструктора
        // Эта строка присваивает значение параметра treatment полю treatment объекта

        this.medications = medications;
        // "medications" — поле класса
        // "medications" — параметр конструктора
        // Эта строка присваивает значение параметра medications полю medications объекта

        this.ward = ward;
        // "ward" — поле класса
        // "ward" — параметр конструктора
        // Эта строка присваивает значение параметра ward полю ward объекта

        this.admissionDate = admissionDate;
        // "admissionDate" — поле класса
        // "admissionDate" — параметр конструктора
        // Эта строка присваивает значение параметра admissionDate полю admissionDate объекта

        this.admissionCount = admissionCount;
        // "admissionCount" — поле класса
        // "admissionCount" — параметр конструктора
        // Эта строка присваивает значение параметра admissionCount полю admissionCount объекта
    }

    public int getId() {
        // "public" — модификатор доступа
        // "int" — тип возвращаемого значения (целое число)
        // "getId" — имя метода, следует соглашению о геттерах (get + имя поля)
        // Скобки () — метод не принимает параметров
        // Этот метод возвращает значение поля id

        return id;
        // "return" — ключевое слово, возвращает значение из метода
        // "id" — поле класса
        // Эта строка возвращает значение поля id
    }
    // "}" — конец метода getId

    // Геттер для поля firstName
    public String getFirstName() {
        // "String" — тип возвращаемого значения (строка)
        // "getFirstName" — имя метода
        // Этот метод возвращает значение поля firstName

        return firstName;
        // "firstName" — поле класса
        // Эта строка возвращает значение поля firstName
    }
    // "}" — конец метода getFirstName

    // Геттер для поля lastName
    public String getLastName() {
        // "getLastName" — имя метода
        // Этот метод возвращает значение поля lastName

        return lastName;
        // "lastName" — поле класса
        // Эта строка возвращает значение поля lastName
    }
    // "}" — конец метода getLastName

    // Геттер для поля middleName
    public String getMiddleName() {
        // "getMiddleName" — имя метода
        // Этот метод возвращает значение поля middleName

        return middleName;
        // "middleName" — поле класса
        // Эта строка возвращает значение поля middleName
    }
    // "}" — конец метода getMiddleName

    // Геттер для поля dateOfBirth
    public String getDateOfBirth() {
        // "getDateOfBirth" — имя метода
        // Этот метод возвращает значение поля dateOfBirth

        return dateOfBirth;
        // "dateOfBirth" — поле класса
        // Эта строка возвращает значение поля dateOfBirth
    }
    // "}" — конец метода getDateOfBirth

    // Геттер для поля phone
    public String getPhone() {
        // "getPhone" — имя метода
        // Этот метод возвращает значение поля phone

        return phone;
        // "phone" — поле класса
        // Эта строка возвращает значение поля phone
    }
    // "}" — конец метода getPhone

    // Геттер для поля email
    public String getEmail() {
        // "getEmail" — имя метода
        // Этот метод возвращает значение поля email

        return email;
        // "email" — поле класса
        // Эта строка возвращает значение поля email
    }
    public String getAddress() {
        // "getAddress" — имя метода
        // Этот метод возвращает значение поля address

        return address;
        // "address" — поле класса
        // Эта строка возвращает значение поля address
    }
    // "}" — конец метода getAddress

    // Геттер для поля doctorId
    public int getDoctorId() {
        // "getDoctorId" — имя метода
        // Этот метод возвращает значение поля doctorId

        return doctorId;
        // "doctorId" — поле класса
        // Эта строка возвращает значение поля doctorId
    }
    // "}" — конец метода getDoctorId

    // Геттер для поля diagnosis
    public String getDiagnosis() {
        // "getDiagnosis" — имя метода
        // Этот метод возвращает значение поля diagnosis

        return diagnosis;
        // "diagnosis" — поле класса
        // Эта строка возвращает значение поля diagnosis
    }
    // "}" — конец метода getDiagnosis

    // Геттер для поля treatment
    public String getTreatment() {
        // "getTreatment" — имя метода
        // Этот метод возвращает значение поля treatment

        return treatment;
        // "treatment" — поле класса
        // Эта строка возвращает значение поля treatment
    }
    // "}" — конец метода getTreatment

    // Геттер для поля medications
    public String getMedications() {
        // "getMedications" — имя метода
        // Этот метод возвращает значение поля medications

        return medications;
        // "medications" — поле класса
        // Эта строка возвращает значение поля medications
    }
    // "}" — конец метода getMedications

    // Геттер для поля ward
    public String getWard() {
        // "getWard" — имя метода
        // Этот метод возвращает значение поля ward

        return ward;
        // "ward" — поле класса
        // Эта строка возвращает значение поля ward
    }
    // "}" — конец метода getWard

    // Геттер для поля admissionDate
    public String getAdmissionDate() {
        // "getAdmissionDate" — имя метода
        // Этот метод возвращает значение поля admissionDate

        return admissionDate;
        // "admissionDate" — поле класса
        // Эта строка возвращает значение поля admissionDate
    }
    // "}" — конец метода getAdmissionDate

    // Геттер для поля admissionCount
    public int getAdmissionCount() {
        // "getAdmissionCount" — имя метода
        // Этот метод возвращает значение поля admissionCount

        return admissionCount;
        // "admissionCount" — поле класса
        // Эта строка возвращает значение поля admissionCount
    }
    // "}" — конец метода getAdmissionCount
}
