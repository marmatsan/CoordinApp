package com.elcazadordebaterias.coordinapp.utils;

import java.util.ArrayList;

public class PetitionRequest {

    private String course;
    private String subject;
    private ArrayList<PetitionUser> petitionUsersList;

    public PetitionRequest(){

    }

    public PetitionRequest(String course, String subject, ArrayList<PetitionUser> petitionUsersList){
        this.course = course;
        this.subject = subject;
        this.petitionUsersList = petitionUsersList;
    }

    public String getCourse() {
        return course;
    }

    public String getSubject() {
        return subject;
    }

    public ArrayList<PetitionUser> getPetitionUsersList() {
        return petitionUsersList;
    }



}
