package com.elcazadordebaterias.coordinapp.utils;

import java.util.List;

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

    public void setSubjectName(String subjectName) {
        mSubjectName = subjectName;
    }

    public List<CourseParticipant> getCourseParticipantList()
    {
        return mCourseParticipantList;
    }

    public void setCourseParticipantList(List<CourseParticipant> courseParticipantList) {
        this.mCourseParticipantList = courseParticipantList;
    }
}
