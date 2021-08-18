package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;

import java.util.ArrayList;

/**
 * Represents a petition request in firestore
 *
 */
public class PetitionRequest {

    private String requesterId;
    private String requesterName;
    private String teacherId;
    private ArrayList<String> petitionUsersIds;
    private ArrayList<PetitionUser> petitionUsersList;

    public PetitionRequest(){

    }

    public PetitionRequest(String requesterId, String requesterName, String teacherId, ArrayList<String> petitionUsersIds, ArrayList<PetitionUser> petitionUsersList){
        this.requesterId = requesterId;
        this.requesterName = requesterName;
        this.teacherId = teacherId;
        this.petitionUsersIds = petitionUsersIds;
        this.petitionUsersList = petitionUsersList;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public ArrayList<String> getPetitionUsersIds() {
        return petitionUsersIds;
    }

    public ArrayList<PetitionUser> getPetitionUsersList() {
        return petitionUsersList;
    }

}
