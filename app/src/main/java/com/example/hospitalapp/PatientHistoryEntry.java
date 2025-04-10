package com.example.hospitalapp;

public class PatientHistoryEntry {
    // "public" — модификатор доступа, класс доступен из любого места в приложении
// "class" — ключевое слово для объявления класса
// "PatientHistoryEntry" — имя класса, отражает его назначение (запись истории пациента)
// Фигурные скобки {} — начало тела класса
// Этот класс будет представлять одну запись из таблицы PatientHistory
    private int id;
    // "private" — модификатор доступа, поле доступно только внутри класса
    // "int" — тип данных, целое число (32-битное, например, 1, 2, 3)
    // "Id" — имя переменной, будет хранить уникальный идентификатор записи истории
    // Точка с запятой (;) — завершает строку кода
    // Это поле хранит ID записи из столбца history_id
    private int patientId;
    // "int" — тип данных, целое число
    // "patientId" — имя переменной, будет хранить идентификатор пациента
    // Это поле хранит ID пациента из столбца patient_id
    private String diagnosis;
    // "String" — тип данных, класс для работы со строками (например, "ОРВИ")
    // "diagnosis" — имя переменной, будет хранить диагноз пациента
    // Это поле хранит диагноз из столбца diagnosis
    private String treatment;
    // "treatment" — имя переменной, будет хранить метод лечения
    // Это поле хранит метод лечения из столбца treatment
    private String medications;
    // "medications" — имя переменной, будет хранить список лекарств
    // Это поле хранит лекарства из столбца medications
    private String ward;
    // "ward" — имя переменной, будет хранить номер палаты
    // Это поле хранит палату из столбца ward
    private String updateDate;
    // "updateDate" — имя переменной, будет хранить дату обновления
    // Это поле хранит дату обновления из столбца update_date

    public PatientHistoryEntry(int id, int patientId, String diagnosis, String treatment, String medications, String ward, String updateDate) {
        // "public" — модификатор доступа, конструктор доступен из любого места
        // "PatientHistoryEntry" — имя конструктора, совпадает с именем класса
        // Скобки () — содержат параметры конструктора
        // "int historyId" — параметр, идентификатор записи
        // "int patientId" — параметр, идентификатор пациента
        // "String diagnosis" — параметр, диагноз
        // "String treatment" — параметр, лечение
        // "String medications" — параметр, лекарства
        // "String ward" — параметр, палата
        // "String updateDate" — параметр, дата обновления
        // Запятая (,) — разделяет параметры
        // Фигурные скобки {} — начало тела конструктора
        // Этот конструктор инициализирует объект класса
        this.id = id;
        // "this" — ключевое слово, ссылается на текущий объект класса
        // "Id" — поле класса
        // "=" — оператор присваивания
        // "Id" — параметр конструктора
        // Эта строка присваивает значение параметра Id полю класса Id
        this.patientId = patientId;
        // "patientId" — поле класса
        // "patientId" — параметр конструктора
        // Эта строка присваивает значение параметра patientId полю класса patientId

        this.diagnosis = diagnosis;
        // "diagnosis" — поле класса
        // "diagnosis" — параметр конструктора
        // Эта строка присваивает значение параметра diagnosis полю класса diagnosis
        this.treatment = treatment;
        // "treatment" — поле класса
        // "treatment" — параметр конструктора
        // Эта строка присваивает значение параметра treatment полю класса treatment

        this.medications = medications;
        // "medications" — поле класса
        // "medications" — параметр конструктора
        // Эта строка присваивает значение параметра medications полю класса medications

        this.ward = ward;
        // "ward" — поле класса
        // "ward" — параметр конструктора
        // Эта строка присваивает значение параметра ward полю класса ward

        this.updateDate = updateDate;
        // "updateDate" — поле класса
        // "updateDate" — параметр конструктора
        // Эта строка присваивает значение параметра updateDate полю класса updateDate
    }

    public int getId() { return id; }
    // "public" — модификатор доступа
    // "int" — тип возвращаемого значения (целое число)
    // "getId" — имя метода, следует соглашению (get + имя поля)
    // Скобки () — метод не принимает параметров
    // Этот метод возвращает значение поля Id
    // "return" — ключевое слово, возвращает значение
    // "Id" — поле класса
    // Эта строка возвращает значение поля Id
    public int getPatientId() { return patientId; }
    public String getDiagnosis() {
        // "String" — тип возвращаемого значения (строка)
        // "getDiagnosis" — имя метода
        // Этот метод возвращает значение поля diagnosis

        return diagnosis;
        // "diagnosis" — поле класса
        // Эта строка возвращает значение поля diagnosis
    }
    public String getTreatment() { return treatment; }
    public String getMedications() { return medications; }
    public String getWard() { return ward; }
    public String getUpdateDate() { return updateDate; }
}
