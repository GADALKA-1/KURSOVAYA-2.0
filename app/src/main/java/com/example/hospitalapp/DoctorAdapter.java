package com.example.hospitalapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DoctorAdapter extends ArrayAdapter<Doctor> {
    public DoctorAdapter(Context context, List<Doctor> doctors) {
        super(context, 0, doctors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        Doctor doctor = getItem(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(doctor.getLastName() + " " + doctor.getFirstName() + " " + doctor.getMiddleName() + " (" + doctor.getSpecialization() + ")");

        return convertView;
    }
}
