package com.elcazadordebaterias.coordinapp.utils.cards;


/**
 * Represents a petition card to create a group.
 *
 * @author Martín Mateos Sánchez
 */
public class PetitionGroupParticipantCard {

    private String participantName;
    private int petitionStatusImage;

    public PetitionGroupParticipantCard(String participantName, int petitionStatusImage){
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
