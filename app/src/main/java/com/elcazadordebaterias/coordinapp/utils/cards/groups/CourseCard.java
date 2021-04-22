package com.elcazadordebaterias.coordinapp.utils.cards.groups;

import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.courses.CourseCardAdapter;

import java.util.ArrayList;
import java.util.List;

public class CourseCard {

    private String mCourseName;
    private ArrayList<CourseSubjectCard> mSubjectList;

    public CourseCard(String courseName, ArrayList<CourseSubjectCard> subjectList) {
        this.mCourseName = courseName;
        this.mSubjectList = subjectList;
    }

    public String getCourseName() {
        return mCourseName;
    }

    public ArrayList<CourseSubjectCard> getSubjectList() {
        return mSubjectList;
    }

}
