package com.example.hospitalapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PatientAdapter extends BaseAdapter {
    private Context context;
    private List<Patient> patientList;
    private List<Boolean> expandedStates;
    private HospitalDbHelper dbHelper;

    public PatientAdapter(Context context, List<Patient> patients) {
        this.context = context;
        this.patientList = patients;
        this.expandedStates = new ArrayList<>();
        this.dbHelper = new HospitalDbHelper(context);
        updateExpandedStates();
    }

    // Метод для обновления списка пациентов и синхронизации expandedStates
    public void updatePatients(List<Patient> newPatients) {
        this.patientList.clear();
        this.patientList.addAll(newPatients);
        updateExpandedStates();
        notifyDataSetChanged();
    }

    // Синхронизируем expandedStates с patientList
    private void updateExpandedStates() {
        expandedStates.clear();
        for (int i = 0; i < patientList.size(); i++) {
            expandedStates.add(false); // Все элементы изначально свёрнуты
        }
    }

    @Override
    public int getCount() {
        return patientList.size();
    }

    @Override
    public Object getItem(int position) {
        return patientList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_patient, parent, false);
            holder = new ViewHolder();
            holder.patientNameTextView = convertView.findViewById(R.id.patientNameTextView);
            holder.dateOfBirthTextView = convertView.findViewById(R.id.dateOfBirthTextView);
            holder.phoneTextView = convertView.findViewById(R.id.phoneTextView);
            holder.emailTextView = convertView.findViewById(R.id.emailTextView);
            holder.addressTextView = convertView.findViewById(R.id.addressTextView);
            holder.doctorTextView = convertView.findViewById(R.id.doctorTextView);
            holder.diagnosisTextView = convertView.findViewById(R.id.diagnosisTextView);
            holder.treatmentTextView = convertView.findViewById(R.id.treatmentTextView);
            holder.medicationsTextView = convertView.findViewById(R.id.medicationsTextView);
            holder.wardTextView = convertView.findViewById(R.id.wardTextView);
            holder.expandableLayout = convertView.findViewById(R.id.expandableLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Patient patient = patientList.get(position);

        // Устанавливаем основную информацию
        holder.patientNameTextView.setText(patient.getFirstName() + " " + patient.getLastName());
        holder.dateOfBirthTextView.setText("Дата рождения: " + patient.getDateOfBirth());
        holder.phoneTextView.setText("Телефон: " + patient.getPhone());
        holder.emailTextView.setText("Email: " + patient.getEmail());
        holder.addressTextView.setText("Адрес: " + patient.getAddress());
        holder.diagnosisTextView.setText("Диагноз: " + patient.getDiagnosis());
        holder.treatmentTextView.setText("Метод лечения: " + patient.getTreatment());
        holder.medicationsTextView.setText("Лекарства: " + patient.getMedications());
        holder.wardTextView.setText("Палата: " + patient.getWard());

        // Получаем имя врача по doctor_id
        String doctorName = getDoctorName(patient.getDoctorId());
        holder.doctorTextView.setText("Лечащий врач: " + doctorName);

        // Управляем видимостью разворачиваемой части
        boolean isExpanded = expandedStates.get(position);
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        // Обработчик клика для разворачивания/сворачивания
        convertView.setOnClickListener(v -> {
            expandedStates.set(position, !isExpanded);
            notifyDataSetChanged();
        });

        return convertView;
    }

    // Метод для получения имени врача по doctor_id
    private String getDoctorName(int doctorId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Doctors", new String[]{"first_name", "last_name"},
                "doctor_id = ?", new String[]{String.valueOf(doctorId)},
                null, null, null);
        String doctorName = "Неизвестный врач";
        if (cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            doctorName = firstName + " " + lastName;
        }
        cursor.close();
        return doctorName;
    }

    // ViewHolder для оптимизации
    private static class ViewHolder {
        TextView patientNameTextView;
        TextView dateOfBirthTextView;
        TextView phoneTextView;
        TextView emailTextView;
        TextView addressTextView;
        TextView doctorTextView;
        TextView diagnosisTextView;
        TextView treatmentTextView;
        TextView medicationsTextView;
        TextView wardTextView;
        LinearLayout expandableLayout;
    }
}
