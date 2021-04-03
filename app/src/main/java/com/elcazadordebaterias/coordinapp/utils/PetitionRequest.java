package com.elcazadordebaterias.coordinapp.utils;

import java.util.ArrayList;

public class PetitionRequest {

    private String course;
    private String subject;
    private String requesterId;
    private String requesterName;
    private ArrayList<PetitionUser> petitionUsersList;
    private ArrayList<String> petitionUsersIds;

    public PetitionRequest(){

    }

    public PetitionRequest(String course, String subject, String requesterId, String requesterName, ArrayList<String> petitionUsersIds, ArrayList<PetitionUser> petitionUsersList){
        this.course = course;
        this.subject = subject;
        this.requesterId = requesterId;
        this.requesterName = requesterName;
        this.petitionUsersIds = petitionUsersIds;
        this.petitionUsersList = petitionUsersList;
    }

    public String getCourse() {
        return course;
    }

    public String getSubject() {
        return subject;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public ArrayList<String> getPetitionUsersIds() {
        return petitionUsersIds;
    }

    public ArrayList<PetitionUser> getPetitionUsersList() {
        return petitionUsersList;
    }



}
