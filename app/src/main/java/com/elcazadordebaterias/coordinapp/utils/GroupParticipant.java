package com.elcazadordebaterias.coordinapp.utils;

public class GroupParticipant {

    private String participantFullName;
    private boolean participantAsTeacher;
    private String participantId;

    public GroupParticipant(){

    }

    public GroupParticipant(String participantFullName, boolean participantAsTeacher, String participantId) {
        this.participantFullName = participantFullName;
        this.participantAsTeacher = participantAsTeacher;
        this.participantId = participantId;
    }

    public String getParticipantFullName() {
        return participantFullName;
    }

    public void setParticipantFullName(String participantFullName) {
        this.participantFullName = participantFullName;
    }

    public boolean isParticipantAsTeacher() {
        return participantAsTeacher;
    }

    public void setParticipantAsTeacher(boolean participantAsTeacher) {
        this.participantAsTeacher = participantAsTeacher;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }
}
