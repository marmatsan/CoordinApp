package com.elcazadordebaterias.coordinapp.utils.cards;

import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.CourseSubjectAdapter;

import java.util.List;

/**
 * Expandable card that displays the participants of the subject of the current course.
 * Used for both teacher and student.
 *
 * The layout of this object is {@link com.elcazadordebaterias.coordinapp.R.layout#utils_coursesubject}
 *
 * @see CourseSubjectAdapter
 * @author Martín Mateos Sánchez
 */

public class CourseSubjectCard {

    private String mSubjectName;
    private List<CourseParticipantCard> mCourseParticipantList;

    public CourseSubjectCard(String subjectName, List<CourseParticipantCard> courseParticipantList) {
        this.mSubjectName = subjectName;
        this.mCourseParticipantList = courseParticipantList;
    }

    public String getSubjectName()
    {
        return mSubjectName;
    }

    public List<CourseParticipantCard> getCourseParticipantList()
    {
        return mCourseParticipantList;
    }

}
