package com.elcazadordebaterias.coordinapp.utils.cards.groups;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Class that represents a group for both the group chat (between students) and single chat (between students and teacher)
 * The layout of this object is {@link com.elcazadordebaterias.coordinapp.R.layout#utils_cards_groupcardteacher}
 *
 * @author Martín Mateos Sánchez
 */

public class GroupCard {

    private String groupName;
    private String groupId; // The id of the document for this group in Firebase
    private String spokerID;
    private String spokerName;
    private String courseName;
    private String subjectName;
    private int numMessages;

    private boolean hasTeacher;

    private ArrayList<String> participantNames;
    private String collectionId;

    public GroupCard(String groupName, String groupId, String courseName, String subjectName, boolean hasTeacher, ArrayList<String> participantNames, String collectionId, String spokerID, String spokerName, int numMessages) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.courseName = courseName;
        this.subjectName = subjectName;
        this.hasTeacher = hasTeacher;
        this.participantNames = participantNames;
        this.collectionId = collectionId;
        this.spokerID = spokerID;
        this.spokerName = spokerName;
        this.numMessages = numMessages;
    }

    public GroupCard(String groupName, String groupId, String courseName, String subjectName, boolean hasTeacher, ArrayList<String> participantNames, String collectionId) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.courseName = courseName;
        this.subjectName = subjectName;
        this.hasTeacher = hasTeacher;
        this.participantNames = participantNames;
        this.collectionId = collectionId;
    }

    public GroupCard(String groupName, String groupId, String courseName, String subjectName, boolean hasTeacher, ArrayList<String> participantNames, String collectionId, String spokerID, String spokerName) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.courseName = courseName;
        this.subjectName = subjectName;
        this.hasTeacher = hasTeacher;
        this.participantNames = participantNames;
        this.collectionId = collectionId;
        this.spokerID = spokerID;
        this.spokerName = spokerName;
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

    public boolean getHasTeacher() {
        return hasTeacher;
    }

    public ArrayList<String> getParticipantNames() {
        Collections.sort(participantNames);
        return participantNames;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public String getSpokerID() {
        return spokerID;
    }

    public String getSpokerName() {
        return spokerName;
    }

    public int getNumMessages() {
        return numMessages;
    }

    public void setNumMessages(int numMessages) {
        this.numMessages = numMessages;
    }
}
