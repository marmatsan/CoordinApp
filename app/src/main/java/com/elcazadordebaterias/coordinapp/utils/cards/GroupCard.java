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
    private String groupId; // The id of the document for this group in Firebase. It contain a collection called "Groups", where the actual groups are, or the id of the group if it is in the IndividualGroups folder
    private String courseName;
    private String subjectName;
    private ArrayList<String> participantNames;
    private String collectionId;

    private int identifier1;
    private int identifier2;

    public GroupCard(String groupName, String groupId, String courseName, String subjectName, ArrayList<String> participantNames, String collectionId, int identifier1, int identifier2) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.courseName = courseName;
        this.subjectName = subjectName;
        this.participantNames = participantNames;
        this.collectionId = collectionId;
        this.identifier1 = identifier1;
        this.identifier2 = identifier2;
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

    public int getIdentifier1() {
        return identifier1;
    }

    public int getIdentifier2() {
        return identifier2;
    }
}
