package com.example.hospitalapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PatientAdapter extends ArrayAdapter<Patient> {
    public PatientAdapter(Context context, List<Patient> patients) {
        super(context, 0, patients);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Проверка на пустой список
        if (getCount() == 0) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText("Нет данных");
            return convertView;
        }

        // Проверка на корректность позиции
        if (position < 0 || position >= getCount()) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText("Некорректная позиция");
            return convertView;
        }

        // Получение объекта пациента по позиции
        Patient patient = getItem(position);
        if (patient == null) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText("Данные отсутствуют");
            return convertView;
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // Установка текста для отображения
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(patient.getFirstName() + " " + patient.getLastName());
        return convertView;
    }
}
