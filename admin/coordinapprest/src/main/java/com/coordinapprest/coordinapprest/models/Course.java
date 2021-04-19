package com.coordinapprest.coordinapprest.models;

import com.coordinapprest.coordinapprest.models.CourseParticipantsIDs;
import java.util.ArrayList;

public class Course {
    
    private String courseName;
    private CourseParticipantsIDs courseParticipantsIDs;
    private ArrayList<Subject> courseSubjects;

    public Course() {
        super();
    }

    public Course(String courseName, CourseParticipantsIDs courseParticipantsIDs, ArrayList<Subject> courseSubjects) {
        this.courseName = courseName;
        this.courseParticipantsIDs = courseParticipantsIDs;
        this.courseSubjects = courseSubjects;
    }


    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public CourseParticipantsIDs getCourseParticipantsIDs() {
        return this.courseParticipantsIDs;
    }

    public void setCourseParticipantsIDs(CourseParticipantsIDs courseParticipantsIDs) {
        this.courseParticipantsIDs = courseParticipantsIDs;
    }

    public ArrayList<Subject> getCourseSubjects() {
        return this.courseSubjects;
    }

    public void setCourseSubjects(ArrayList<Subject> courseSubjects) {
        this.courseSubjects = courseSubjects;
    }

}
