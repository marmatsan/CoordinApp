package com.elcazadordebaterias.coordinapp.utils.cards.groups;

import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.courses.CourseSubjectAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.courses.CourseParticipantCard;

import java.util.ArrayList;
import java.util.List;

public class CourseSubjectCard {

    private String subjectName;
    private ArrayList<GroupCard> groupsList;

    public CourseSubjectCard(String subjectName, ArrayList<GroupCard> groupsList) {
        this.subjectName = subjectName;
        this.groupsList = groupsList;
    }

    public String getSubjectName()
    {
        return subjectName;
    }

    public ArrayList<GroupCard> getGroupsList()
    {
        return groupsList;
    }

}
