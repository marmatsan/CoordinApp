package com.elcazadordebaterias.coordinapp.utils;

public class PetitionUser {

    private String userId;
    private int petitionStatus;

    public PetitionUser(){

    }

    public PetitionUser(String userId, int petitionStatus){
        this.userId = userId;
        this.petitionStatus = petitionStatus;
    }

    public String getUserId() {
        return userId;
    }

    public int getPetitionStatus() {
        return petitionStatus;
    }
}
