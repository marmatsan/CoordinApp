package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;

public class GroupParticipant {

    private String participantFullName;
    private String participantId;

    public GroupParticipant(){

    }

    public GroupParticipant(String participantFullName, String participantId) {
        this.participantFullName = participantFullName;
        this.participantId = participantId;
    }

    public String getParticipantFullName() {
        return participantFullName;
    }

    public void setParticipantFullName(String participantFullName) {
        this.participantFullName = participantFullName;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }
}
