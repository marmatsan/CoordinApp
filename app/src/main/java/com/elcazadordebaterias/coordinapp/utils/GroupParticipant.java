package com.elcazadordebaterias.coordinapp.utils;

public class GroupParticipant {

    private String participantName;
    private int petitionStatusImage;

    public GroupParticipant(String participantName, int petitionStatusImage){
        this.participantName = participantName;
        this.petitionStatusImage = petitionStatusImage;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public int getPetitionStatusImage() {
        return petitionStatusImage;
    }

    public void setPetitionStatusImage(int petitionStatusImage) {
        this.petitionStatusImage = petitionStatusImage;
    }

}
