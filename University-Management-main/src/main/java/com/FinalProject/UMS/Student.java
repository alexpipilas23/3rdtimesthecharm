package com.FinalProject.UMS;

import java.util.List;

public class Student extends User {
    private String lastName;
    private String firstName;
    private String email;
    private String role;
    private String id;
    private String address;
    private String telephone;
    private String profilePhoto;
    private String subjectsRegistered;
    private String thesisTitle;
    private double progress; // Change progress to double
    private String password;

    public Student(String id, String firstName, String lastName, String address, String telephone, String email,
                   String academicLevel, String currentSemester, String profilePhoto, String subjectsRegistered,
                   String thesisTitle, double progress, String password) {
        super(); // Not passing password here, so User's password remains unset.
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
        this.role = academicLevel; // Assuming academicLevel is the role
        this.profilePhoto = profilePhoto;
        this.subjectsRegistered = subjectsRegistered;
        this.thesisTitle = thesisTitle;
        this.progress = progress;
        this.password = password; // Setting Student's own password field.
    }

    // Override the authenticate method to use the Student's password field.
    @Override
    public boolean authenticate(String password) {
        if (this.password == null || password == null) {
            return false;
        }
        return this.password.equals(password);
    }

}
