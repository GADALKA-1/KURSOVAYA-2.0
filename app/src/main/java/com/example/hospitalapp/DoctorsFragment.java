package com.example.hospitalapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class DoctorsFragment extends Fragment {
    private HospitalDbHelper dbHelper;
    private List<Doctor> doctorList;
    private DoctorAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctors, container, false);

        dbHelper = new HospitalDbHelper(getContext());
        doctorList = new ArrayList<>();
        adapter = new DoctorAdapter(getContext(), doctorList);

        ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddDoctorActivity.class);
            startActivity(intent);
        });

        loadDoctors();
        return view;
    }

    private void loadDoctors() {
        doctorList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Doctors", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            String specialization = cursor.getString(cursor.getColumnIndexOrThrow("specialization"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            doctorList.add(new Doctor(id, firstName, lastName, specialization, phone, email));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDoctors();
    }
}
