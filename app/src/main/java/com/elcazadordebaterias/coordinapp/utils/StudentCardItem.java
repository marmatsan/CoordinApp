package com.elcazadordebaterias.coordinapp.utils;

public class StudentCardItem {

    private String studentName;
    private String studentEmail;

    public StudentCardItem(String studentName, String studentEmail){
        this.studentName = studentName;
        this.studentEmail = studentEmail;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }
}
