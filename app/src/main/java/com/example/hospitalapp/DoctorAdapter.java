package com.example.hospitalapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DoctorAdapter extends ArrayAdapter<Doctor> {
    // Объявление класса DoctorAdapter, который наследуется от ArrayAdapter<Doctor>.
// "public" — модификатор доступа, делает класс доступным из других классов.
// "class" — ключевое слово для объявления класса.
// "DoctorAdapter" — имя класса, отражает его назначение (адаптер для списка врачей).
// "extends" — ключевое слово, указывает, что класс наследуется от другого класса.
// "ArrayAdapter" — базовый класс, адаптер для списков.
// "<Doctor>" — обобщение (generic), указывает, что адаптер будет работать с объектами типа Doctor.
    public DoctorAdapter(Context context, List<Doctor> doctors) {
        // Конструктор класса DoctorAdapter.
        // "public" — модификатор доступа, делает конструктор доступным из других классов.
        // "DoctorAdapter" — имя конструктора, совпадает с именем класса.
        // "Context" — тип параметра, объект контекста приложения.
        // "context" — имя параметра, будет содержать контекст (например, активность).
        // "List<Doctor>" — тип параметра, список объектов типа Doctor.
        // "doctors" — имя параметра, будет содержать список врачей.
        super(context, 0, doctors);
        // Вызов конструктора родительского класса ArrayAdapter.
        // "super" — ключевое слово, обращается к родительскому классу (ArrayAdapter).
        // "context" — параметр, передаём контекст в родительский конструктор.
        // "0" — целое число, идентификатор ресурса макета (0 означает, что мы зададим макет вручную в getView).
        // "doctors" — список врачей, передаём в родительский конструктор для связывания данных.
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Переопределение метода getView для настройки отображения каждого элемента списка.
        // "@Override" — аннотация, указывает, что метод переопределяет метод из родительского класса.
        // "public" — модификатор доступа, делает метод доступным из других классов.
        // "View" — тип возвращаемого значения, метод возвращает объект View (элемент списка).
        // "getView" — имя метода, вызывается для создания или обновления View для каждого элемента списка.
        // "int" — тип параметра, целое число.
        // "position" — имя параметра, позиция элемента в списке (начинается с 0).
        // "View" — тип параметра, объект View.
        // "convertView" — имя параметра, ранее созданный View (может быть null, если элемент создаётся впервые).
        // "ViewGroup" — тип параметра, контейнер.
        // "parent" — имя параметра, родительский контейнер (например, ListView).
        if (convertView == null) {
            // Проверка, существует ли уже View для повторного использования.
            // "if" — ключевое слово для условного оператора.
            // "convertView" — параметр, ранее созданный View.
            // "==" — оператор сравнения, проверяет, равно ли значение null.
            // "null" — ключевое слово, обозначает отсутствие значения.
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            // Создание нового View из XML-макета, если convertView не существует.
            // "convertView" — переменная, в которую сохраняем новый View.
            // "=" — оператор присваивания, присваивает значение справа переменной слева.
            // "LayoutInflater" — класс для создания View из XML.
            // "from" — статический метод класса LayoutInflater, создаёт LayoutInflater из контекста.
            // "getContext" — метод класса ArrayAdapter, возвращает контекст (унаследован от родительского класса).
            // "()" — вызов метода без параметров.
            // "inflate" — метод класса LayoutInflater, создаёт View из XML-ресурса.
            // "android.R.layout.simple_list_item_2" — идентификатор ресурса макета, встроенный макет Android с двумя текстовыми полями.
            // "android" — пространство имён Android.
            // "R" — класс ресурсов Android.
            // "layout" — тип ресурса (макет).
            // "simple_list_item_2" — имя макета, содержит два TextView (text1 и text2).
            // "parent" — родительский контейнер, передаём в метод inflate.
            // "false" — булево значение, указывает, не прикреплять View к parent (это сделает ListView).
        }

        Doctor doctor = getItem(position);
        // Получение объекта врача по позиции в списке.
        // "Doctor" — тип переменной, объект врача.
        // "doctor" — имя переменной, будет хранить объект врача.
        // "=" — оператор присваивания.
        // "getItem" — метод класса ArrayAdapter, возвращает элемент списка по позиции (унаследован от родительского класса).
        // "position" — параметр, позиция элемента в списке.
        TextView textView = convertView.findViewById(android.R.id.text1);
        // Получение ссылки на первое текстовое поле (text1) в макете.
        // "TextView" — тип переменной, виджет для отображения текста.
        // "textView" — имя переменной, будет хранить ссылку на TextView.
        // "=" — оператор присваивания.
        // "convertView" — объект View, созданный из макета.
        // "findViewById" — метод класса View, находит элемент по идентификатору.
        // "android.R.id.text1" — идентификатор ресурса, встроенный идентификатор для первого TextView в simple_list_item_2.
        // "android" — пространство имён Android.
        // "R" — класс ресурсов.
        // "id" — тип ресурса (идентификатор).
        // "text1" — имя идентификатора.
        textView.setText(doctor.getLastName() + " " + doctor.getFirstName() + " " + doctor.getMiddleName() + " (" + doctor.getSpecialization() + ")");

        return convertView;
    }
}
