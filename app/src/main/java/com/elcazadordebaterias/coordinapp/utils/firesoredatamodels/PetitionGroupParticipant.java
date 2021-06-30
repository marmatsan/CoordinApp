package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;


/**
 * Represents a petition card to create a group in firestore
 *
 * @author Martín Mateos Sánchez
 */
public class PetitionGroupParticipant {

    private String participantName;
    private int petitionStatusImage;

    public PetitionGroupParticipant(String participantName, int petitionStatusImage){
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
