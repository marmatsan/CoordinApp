package com.elcazadordebaterias.coordinapp.utils;

import java.util.ArrayList;

public class Group {

    private String groupId; // The id of the group document in FireStore
    private String coordinatorId; // The id of the teacher that accepted the group

    private String groupName;
    private String subjectName;

    private ArrayList<String> participantsIds;

    public Group(){

    }

    public Group(String groupId, String coordinatorId, String groupName, String subjectName, ArrayList<String> participantsIds){
        this.groupId = groupId;
        this.coordinatorId = coordinatorId;
        this.groupName = groupName;
        this.subjectName = subjectName;
        this.participantsIds = participantsIds;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getCoordinatorId() {
        return coordinatorId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public ArrayList<String> getParticipantsIds() {
        return participantsIds;
    }
}
