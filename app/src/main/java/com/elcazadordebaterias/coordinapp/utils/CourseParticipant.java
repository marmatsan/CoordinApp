package com.elcazadordebaterias.coordinapp.utils;

public class CourseParticipant {

    private String mParticipantName;
    private String mParticipantEmail;

    public CourseParticipant(String participantName, String participantEmail) {
        this.mParticipantName = participantName;
        this.mParticipantEmail = participantEmail;
    }

    public String getParticipantName() {
        return mParticipantName;
    }

    public String getParticipantEmail() {
        return mParticipantEmail;
    }

}

