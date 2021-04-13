package com.elcazadordebaterias.coordinapp.utils;

public class PetitionGroupCardParticipant {

    private String participantName;
    private int petitionStatusImage;

    public PetitionGroupCardParticipant(String participantName, int petitionStatusImage){
        this.participantName = participantName;
        this.petitionStatusImage = petitionStatusImage;
    }

    public String getParticipantName() {
        return participantName;
    }

    public int getPetitionStatusImage() {
        return petitionStatusImage;
    }

}
