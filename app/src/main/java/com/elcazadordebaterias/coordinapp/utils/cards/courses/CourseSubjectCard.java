package com.elcazadordebaterias.coordinapp.utils.cards.courses;

import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.courses.CourseSubjectAdapter;

import java.util.List;

/**
 * Expandable card that displays the participants of the subject of the current course. Do not confuse with
 * {@link com.elcazadordebaterias.coordinapp.utils.cards.groups.CourseSubjectCard}, which contains a list of
 * the groups of the current subject. This card contains the list of the participants of the subject instead.
 *
 * Used for both teacher and student.
 *
 * Layout: {@link com.elcazadordebaterias.coordinapp.R.layout#utils_coursesubject}
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
