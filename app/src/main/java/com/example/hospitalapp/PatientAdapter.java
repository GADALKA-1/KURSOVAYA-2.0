package com.example.hospitalapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
// Импортируем классы для работы со списками
// "java.util.List" — интерфейс List из стандартной библиотеки Java, определяет методы для работы со списками
// "java.util" — пакет для утилит, "List" — интерфейс для списков
public class PatientAdapter extends ArrayAdapter<Patient> {
    // Объявляем класс PatientAdapter, который наследуется от ArrayAdapter<Patient>
// "public" — модификатор доступа, означает, что класс доступен из любого другого класса
// "class" — ключевое слово для объявления класса
// "PatientAdapter" — имя нашего класса, отражает его назначение (адаптер для пациентов)
// "extends" — ключевое слово, указывает, что наш класс наследуется от другого класса
// "ArrayAdapter<Patient>" — базовый класс, от которого мы наследуемся, адаптер для списка объектов типа Patient
// "ArrayAdapter" — класс адаптера
// "<Patient>" — обобщение (generic), указывает, что адаптер работает с объектами типа Patient
    public PatientAdapter(Context context, List<Patient> patients) {
        super(context, 0, patients);
    }
    // Конструктор класса PatientAdapter
    // "public" — модификатор доступа, конструктор доступен из любого класса
    // "PatientAdapter" — имя конструктора, совпадает с именем класса
    // "Context" — тип параметра, контекст приложения
    // "context" — имя параметра, будет передан при создании адаптера
    // "List<Patient>" — тип параметра, список объектов типа Patient
    // "List" — интерфейс для списков
    // "<Patient>" — обобщение, указывает, что список содержит объекты Patient
    // "patients" — имя параметра, список пациентов для отображения
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Переопределяем метод getView, который создаёт View для каждого элемента списка
        // "@Override" — аннотация, указывает, что мы переопределяем метод из родительского класса (ArrayAdapter)
        // "public" — модификатор доступа, метод доступен из любого класса
        // "View" — тип возвращаемого значения, метод возвращает объект View (элемент списка)
        // "getView" — имя метода, вызывается для создания или обновления View для каждого элемента списка
        // "int" — тип параметра, целое число
        // "position" — имя параметра, позиция элемента в списке (начинается с 0)
        // "View" — тип параметра, объект View
        // "convertView" — имя параметра, ранее созданный View (может быть null, если View создаётся впервые)
        // "ViewGroup" — тип параметра, контейнер
        // "parent" — имя параметра, родительский контейнер (ListView)
        if (convertView == null) {
            // Проверяем, можно ли переиспользовать convertView
            // "if" — условный оператор, выполняет код, если условие истинно
            // "convertView" — параметр, ранее созданный View
            // "==" — оператор сравнения, проверяет, равно ли значение null
            // "null" — значение, означающее отсутствие объекта
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        // Создаём новый View из макета
        // "convertView" — наша переменная (параметр)
        // "LayoutInflater" — класс для создания View из XML
        // "from" — статический метод LayoutInflater, создаёт LayoutInflater из контекста
        // "getContext()" — метод ArrayAdapter, возвращает контекст приложения
        // "()" — вызов метода без параметров
        // "inflate" — метод LayoutInflater, преобразует XML-макет в View
        // "android.R.layout.simple_list_item_2" — идентификатор макета из Android SDK
        // "android.R" — класс ресурсов Android SDK
        // "layout" — подмодуль, содержащий макеты
        // "simple_list_item_2" — имя макета, содержит два TextView (text1 и text2)
        // "parent" — параметр, родительский контейнер (ListView)
        // "false" — булево значение, указывает, что не нужно сразу добавлять View в parent

        Patient patient = getItem(position);
        // Получаем пациента по позиции
        // "Patient" — тип переменной, объект типа Patient
        // "patient" — имя переменной
        // "getItem" — метод ArrayAdapter, возвращает элемент списка по позиции
        // "position" — параметр, позиция элемента
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(patient.getLastName() + " " + patient.getFirstName() + " " + patient.getMiddleName());
        // Устанавливаем текст для text1 (ФИО пациента)
        // "text1" — наш TextView
        // "setText" — метод TextView, задаёт текст
        // "patient" — наш объект Patient
        // "getLastName()" — метод Patient, возвращает фамилию
        // "+" — оператор конкатенации строк
        // " " — пробел
        // "getFirstName()" — метод Patient, возвращает имя
        // "getMiddleName()" — метод Patient, возвращает отчество
        return convertView;
    }
}
