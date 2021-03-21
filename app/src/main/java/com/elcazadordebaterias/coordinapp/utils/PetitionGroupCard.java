package com.elcazadordebaterias.coordinapp.utils;

import java.util.ArrayList;

public class PetitionGroupCard {

    private String requesterName;
    private String courseSubject;
    private ArrayList<GroupParticipant> participantsList;

    public PetitionGroupCard(String requesterName, String courseSubject, ArrayList<GroupParticipant> participantsList){
        this.requesterName = requesterName;
        this.courseSubject = courseSubject;
        this.participantsList = participantsList;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getCourseSubject() {
        return courseSubject;
    }

    public void setCourseSubject(String courseSubject) {
        this.courseSubject = courseSubject;
    }

    public ArrayList<GroupParticipant> getParticipantsList() {
        return participantsList;
    }

    public void setParticipantsList(ArrayList<GroupParticipant> participantsList) {
        this.participantsList = participantsList;
    }
}
