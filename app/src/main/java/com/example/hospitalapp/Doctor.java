package com.example.hospitalapp;

public class Doctor {
    private int id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String specialization;

    public Doctor(int id, String firstName, String lastName, String middleName, String specialization) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.specialization = specialization;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getSpecialization() {
        return specialization;
    }

    @Override
    public String toString() {
        return lastName + " " + firstName + " " + middleName + " (" + specialization + ")";
    }
}
