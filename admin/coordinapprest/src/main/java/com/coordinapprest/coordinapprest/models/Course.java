package com.coordinapprest.coordinapprest.models;

import java.util.ArrayList;

public class Course {
    
    private String courseName;
    private ArrayList<Subject> courseSubjects;

    public Course() {
        super();
    }

    public Course(String courseName, ArrayList<Subject> courseSubjects) {
        this.courseName = courseName;
        this.courseSubjects = courseSubjects;
    }


    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }


    public ArrayList<Subject> getCourseSubjects() {
        return this.courseSubjects;
    }

    public void setCourseSubjects(ArrayList<Subject> courseSubjects) {
        this.courseSubjects = courseSubjects;
    }

}
