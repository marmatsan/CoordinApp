package com.elcazadordebaterias.coordinapp.utils;

import android.content.Intent;

import com.elcazadordebaterias.coordinapp.activities.ChatActivity;
import com.elcazadordebaterias.coordinapp.adapters.GroupalCardAdapter;

import java.util.ArrayList;

/**
 * Class that represents a group for both the groupal chat (between students) and single chat (between students and teacher)
 * The layout of this object is {@link com.elcazadordebaterias.coordinapp.R.layout#utils_groupalcard}
 *
 * @see GroupalCardAdapter
 * @author Martín Mateos Sánchez
 */

public class GroupalCard {

    private String groupId; // The id of the document for this group in FireBase
    private int subjectImage; // The image of the subject of this group.
    private String courseName;
    private String subjectName;
    private ArrayList<String> participantNames;

    public GroupalCard(String groupId, int subjectImage, String courseName, String subjectName, ArrayList<String> participantNames) {
        this.groupId = groupId;
        this.subjectImage = subjectImage;
        this.courseName = courseName;
        this.subjectName = subjectName;
        this.participantNames = participantNames;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getSubjectImage() {
        return subjectImage;
    }

    public void setSubjectImage(int subjectImage) {
        this.subjectImage = subjectImage;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public ArrayList<String> getParticipantNames() {
        return participantNames;
    }

    public void setParticipantNames(ArrayList<String> participantNames) {
        this.participantNames = participantNames;
    }

}
