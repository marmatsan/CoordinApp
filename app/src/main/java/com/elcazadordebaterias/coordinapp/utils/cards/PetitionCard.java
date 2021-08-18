package com.elcazadordebaterias.coordinapp.utils.cards;

import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.PetitionGroupCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionGroupParticipant;

import java.util.ArrayList;

/**
 * Class that represents a petition card.
 * The layout of this object is {@link com.elcazadordebaterias.coordinapp.R.layout#utils_petitiongroupcard}
 *
 * @see PetitionGroupCardAdapter
 * @author Martín Mateos Sánchez
 */

public class PetitionCard {

    private String selectedCourse;
    private String selectedSubject;
    private String petitionId;
    private String requesterName;
    private String requesterId;
    private ArrayList<PetitionGroupParticipant> participantsList;

    public PetitionCard(String selectedCourse, String selectedSubject, String petitionId, String requesterId, String requesterName, ArrayList<PetitionGroupParticipant> participantsList){
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
        this.petitionId = petitionId;
        this.requesterName = requesterName;
        this.requesterId = requesterId;
        this.participantsList = participantsList;
    }

    public String getSelectedCourse() {
        return selectedCourse;
    }

    public String getSelectedSubject() {
        return selectedSubject;
    }

    public String getPetitionId() {
        return petitionId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public ArrayList<PetitionGroupParticipant> getParticipantsList() {
        return participantsList;
    }

    public void setParticipantsList(ArrayList<PetitionGroupParticipant> participantsList) {
        this.participantsList = participantsList;
    }
}
