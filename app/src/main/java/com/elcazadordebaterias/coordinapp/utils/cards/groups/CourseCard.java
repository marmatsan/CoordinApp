package com.elcazadordebaterias.coordinapp.utils.cards.groups;

import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.courses.CourseCardAdapter;

import java.util.ArrayList;

/**
 * Expandable card that displays the subjects an user is inrolled in the current course. It is made by
 * the name of the course and the list of the subjects, which are also cards.
 * Used for both teacher and student.
 *
 * The layout of this object is {@link com.elcazadordebaterias.coordinapp.R.layout#utils_coursecard}
 *
 * @see CourseCardAdapter
 * @author Martín Mateos Sánchez
 */
public class CourseCard {

    private String courseName;
    private ArrayList<CourseSubjectCard> subjectList;

    public CourseCard(String courseName, ArrayList<CourseSubjectCard> subjectList) {
        this.courseName = courseName;
        this.subjectList = subjectList;
    }

    public String getCourseName() {
        return courseName;
    }

    public ArrayList<CourseSubjectCard> getSubjectList() {
        return subjectList;
    }

}
