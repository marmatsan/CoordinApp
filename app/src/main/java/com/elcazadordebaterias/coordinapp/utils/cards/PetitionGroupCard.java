package com.elcazadordebaterias.coordinapp.utils.cards;

import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.PetitionGroupCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.PetitionGroupParticipant;

import java.util.ArrayList;

/**
 * Class that represents a petition card.
 * The layout of this object is {@link com.elcazadordebaterias.coordinapp.R.layout#utils_petitiongroupcard}
 *
 * @see PetitionGroupCardAdapter
 * @author Martín Mateos Sánchez
 */

public class PetitionGroupCard {

    private String petitionId; // The id of the document of this card in FireStore
    private String requesterName;
    private String courseSubject;
    private ArrayList<PetitionGroupParticipant> participantsList;

    public PetitionGroupCard(String petitionId, String requesterName, String courseSubject, ArrayList<PetitionGroupParticipant> participantsList){
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

    public ArrayList<PetitionGroupParticipant> getParticipantsList() {
        return participantsList;
    }

    public void setParticipantsList(ArrayList<PetitionGroupParticipant> participantsList) {
        this.participantsList = participantsList;
    }
}
