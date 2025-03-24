package com.example.hospitalapp;

public class PatientHistoryEntry {
    private int id;
    private int patientId;
    private String diagnosis;
    private String treatment;
    private String medications;
    private String ward;
    private String updateDate;

    public PatientHistoryEntry(int id, int patientId, String diagnosis, String treatment, String medications, String ward, String updateDate) {
        this.id = id;
        this.patientId = patientId;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.medications = medications;
        this.ward = ward;
        this.updateDate = updateDate;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public String getDiagnosis() { return diagnosis; }
    public String getTreatment() { return treatment; }
    public String getMedications() { return medications; }
    public String getWard() { return ward; }
    public String getUpdateDate() { return updateDate; }
}
