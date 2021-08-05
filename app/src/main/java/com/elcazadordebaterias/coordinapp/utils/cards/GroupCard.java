package com.elcazadordebaterias.coordinapp.utils.cards;

import java.util.ArrayList;

/**
 * Class that represents a group for both the group chat (between students) and single chat (between students and teacher)
 * The layout of this object is {@link com.elcazadordebaterias.coordinapp.R.layout#utils_cards_groupcard}
 *
 * @author Martín Mateos Sánchez
 */

public class GroupCard {

    private String groupName;
    private String groupId; // The id of the document for this group in Firebase. It contain a collection called "Groups", where the actual groups are.
    private String courseName;
    private String subjectName;
    private ArrayList<String> participantNames;
    private String collectionId;

    public GroupCard(String groupName, String groupId, String courseName, String subjectName, ArrayList<String> participantNames, String collectionId) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.courseName = courseName;
        this.subjectName = subjectName;
        this.participantNames = participantNames;
        this.collectionId = collectionId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupId() {
        return groupId;
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

    public String getCollectionId() {
        return collectionId;
    }
}
