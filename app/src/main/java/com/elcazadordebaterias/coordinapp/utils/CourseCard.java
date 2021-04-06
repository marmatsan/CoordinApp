package com.elcazadordebaterias.coordinapp.utils;

import java.util.List;

/**
 * Expandable card that displays the subjects an user is inrolled in the current course. It is made by
 * the name of the course and the list of the subjects, which are also cards.
 * Used for both teacher and student.
 *
 * The layout of this object is {@link com.elcazadordebaterias.coordinapp.R.layout#utils_coursecard}
 *
 * @see com.elcazadordebaterias.coordinapp.adapters.CourseCardAdapter
 * @author Martín Mateos Sánchez
 */

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

    public List<CourseSubject> getSubjectList() {
        return mSubjectList;
    }

}
