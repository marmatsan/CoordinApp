package com.elcazadordebaterias.coordinapp.utils;

import java.util.List;

public class CourseCard {

    private String mCourseName;
    private List<CourseSubject> mSubjectList;

    public CourseCard(String courseName, List<CourseSubject> subjectList) {
        this.mCourseName = courseName;
        this.mSubjectList = subjectList;
    }

    public String getCourseName() {
        return mCourseName;
    }

    public void setCourseName(String courseName) {
        mCourseName = courseName;
    }

    public List<CourseSubject> getSubjectList() {
        return mSubjectList;
    }

    public void setSubjectList(List<CourseSubject> subjectList) {
        this.mSubjectList = subjectList;
    }

}
