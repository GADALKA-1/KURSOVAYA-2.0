package com.example.hospitalapp;

public class Patient {
    private int id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String phone;
    private String email;
    private String address;
    private int doctorId;
    private String diagnosis;
    private String treatment;
    private String medications;
    private String ward;

    public Patient(int id, String firstName, String lastName, String dateOfBirth, String phone, String email,
                   String address, int doctorId, String diagnosis, String treatment, String medications, String ward) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.doctorId = doctorId;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.medications = medications;
        this.ward = ward;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public int getDoctorId() { return doctorId; }
    public String getDiagnosis() { return diagnosis; }
    public String getTreatment() { return treatment; }
    public String getMedications() { return medications; }
    public String getWard() { return ward; }
}
