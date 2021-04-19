package com.elcazadordebaterias.coordinapp.utils.restmodel;

import java.util.ArrayList;

public class Subject {

    private ArrayList<String> studentIDs;
    private String teacherID;
    private String subjectName;

    public Subject(){
        super();
    }

    public Subject(ArrayList<String> studentIDs, String teacherID, String subjectName) {
        this.studentIDs = studentIDs;
        this.teacherID = teacherID;
        this.subjectName = subjectName;
    }


    public ArrayList<String> getStudentIDs() {
        return this.studentIDs;
    }

    public void setStudentIDs(ArrayList<String> studentIDs) {
        this.studentIDs = studentIDs;
    }

    public String getTeacherID() {
        return this.teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public String getSubjectName() {
        return this.subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

}
