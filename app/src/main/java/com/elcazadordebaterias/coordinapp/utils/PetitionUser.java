package com.elcazadordebaterias.coordinapp.utils;

public class PetitionUser {

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_ACCEPTED = 1;
    public static final int STATUS_REJECTED = 2;

    private String userId;
    private String userFullName;
    private int petitionStatus;

    public PetitionUser(){

    }

    public PetitionUser(String userId, String userFullName, int petitionStatus){
        this.userId = userId;
        this.userFullName = userFullName;
        this.petitionStatus = petitionStatus;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public int getPetitionStatus() {
        return petitionStatus;
    }

    public void setPetitionStatus(int petitionStatus){
        this.petitionStatus = petitionStatus;
    }
}
