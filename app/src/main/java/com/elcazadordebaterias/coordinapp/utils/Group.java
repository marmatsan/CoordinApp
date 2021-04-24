package com.elcazadordebaterias.coordinapp.utils;

import java.util.ArrayList;

/**
 * Class that represents the data of the group an user is in FireStore. Used when creating a new group,
 * accepted by the teacher.
 *
 * @author Martín Mateos Sánchez
 */

public class Group {

    private String coordinatorId; // The id of the teacher that accepted the group
    private String groupName;     // The name of this group. Do not confuse with the name of the course
    private String subjectName;

    private ArrayList<String> participantsIds;
    private ArrayList<GroupParticipant> participants;

    public Group(){

    }

    public Group(String coordinatorId, String groupName, String subjectName, ArrayList<String> participantsIds, ArrayList<GroupParticipant> participants){
        this.coordinatorId = coordinatorId;
        this.groupName = groupName;
        this.subjectName = subjectName;
        this.participantsIds = participantsIds;
        this.participants = participants;
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

    public ArrayList<GroupParticipant> getParticipants() {
        return participants;
    }
}
