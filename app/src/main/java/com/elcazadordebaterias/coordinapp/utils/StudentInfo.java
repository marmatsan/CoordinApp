package com.elcazadordebaterias.coordinapp.utils;

public class StudentInfo {

    private String studentName;
    private String studentId;

    public StudentInfo(){

    }

    public StudentInfo(String studentName, String studentId){
        this.studentName = studentName;
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentId() {
        return studentId;
    }
}
