package com.elcazadordebaterias.coordinapp.utils;

import android.widget.ListView;

public class PetitionGroupCard {

    private String requesterName;
    private String courseSubject;
    private ListView participantsList;

    public PetitionGroupCard(String requesterName, String courseSubject, ListView participantsList){
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

    public ListView getParticipantsList() {
        return participantsList;
    }

    public void setParticipantsList(ListView participantsList) {
        this.participantsList = participantsList;
    }
}
