package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;

import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.GroupParticipant;

import java.util.ArrayList;

/**
 * Class that represents the data of the group an user is in FireStore. Used when creating a new group,
 * accepted by the teacher.
 *
 * @author Martín Mateos Sánchez
 */
public class Group {

    private String coordinatorId; // The id of the teacher that created the group
    private String coordinatorName; // The name of the teacher that created the group
    private String courseName;     // The name of this group
    private String subjectName;

    private ArrayList<String> participantsIds;
    private ArrayList<GroupParticipant> participants;

    public Group(){

    }

    public Group(String coordinatorId, String coordinatorName, String courseName, String subjectName, ArrayList<String> participantsIds, ArrayList<GroupParticipant> participants){
        this.coordinatorId = coordinatorId;
        this.coordinatorName = coordinatorName;
        this.courseName = courseName;
        this.subjectName = subjectName;
        this.participantsIds = participantsIds;
        this.participants = participants;
    }

    public String getCoordinatorId() {
        return coordinatorId;
    }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public String getCourseName() {
        return courseName;
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
