package com.elcazadordebaterias.coordinapp.utils.cards;

import java.util.ArrayList;

/**
 * Class that represents a petition card.
 * The layout of this object is {@link com.elcazadordebaterias.coordinapp.R.layout#utils_petitiongroupcard}
 *
 * @see com.elcazadordebaterias.coordinapp.adapters.PetitionGroupCardAdapter
 * @author Martín Mateos Sánchez
 */

public class PetitionGroupCard {

    private String petitionId; // The id of the document of this card in FireStore
    private String requesterName;
    private String courseSubject;
    private ArrayList<PetitionGroupParticipantCard> participantsList;

    public PetitionGroupCard(String petitionId, String requesterName, String courseSubject, ArrayList<PetitionGroupParticipantCard> participantsList){
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

    public ArrayList<PetitionGroupParticipantCard> getParticipantsList() {
        return participantsList;
    }

    public void setParticipantsList(ArrayList<PetitionGroupParticipantCard> participantsList) {
        this.participantsList = participantsList;
    }
}
