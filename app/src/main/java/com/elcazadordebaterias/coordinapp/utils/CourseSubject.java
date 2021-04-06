package com.elcazadordebaterias.coordinapp.utils;

import java.util.List;

/**
 * Expandable card that displays the participants of the subject of the current course.
 * Used for both teacher and student.
 *
 * The layout of this object is {@link com.elcazadordebaterias.coordinapp.R.layout#utils_coursesubject}
 *
 * @see com.elcazadordebaterias.coordinapp.adapters.CourseSubjectAdapter
 * @author Martín Mateos Sánchez
 */

public class CourseSubject {

    private String mSubjectName;
    private List<CourseParticipant> mCourseParticipantList;

    public CourseSubject(String subjectName, List<CourseParticipant> courseParticipantList) {
        this.mSubjectName = subjectName;
        this.mCourseParticipantList = courseParticipantList;
    }

    public String getSubjectName()
    {
        return mSubjectName;
    }

    public List<CourseParticipant> getCourseParticipantList()
    {
        return mCourseParticipantList;
    }

}
