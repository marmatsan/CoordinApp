package com.elcazadordebaterias.coordinapp.utils;

import java.util.ArrayList;

public class PetitionGroupCard {

    private String petitionId;
    private String requesterName;
    private String courseSubject;
    private ArrayList<GroupParticipant> participantsList;

    public PetitionGroupCard(String petitionId, String requesterName, String courseSubject, ArrayList<GroupParticipant> participantsList){
        this.petitionId = petitionId;
        this.requesterName = requesterName;
        this.courseSubject = courseSubject;
        this.participantsList = participantsList;
    }

    public String getPetitionId() {
        return petitionId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public String getCourseSubject() {
        return courseSubject;
    }

    public ArrayList<GroupParticipant> getParticipantsList() {
        return participantsList;
    }

}
