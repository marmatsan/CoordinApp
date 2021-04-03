package com.elcazadordebaterias.coordinapp.utils;

public class PetitionUser {

    private String userId;
    private String userFullName;
    private boolean isTeacher;
    private int petitionStatus;

    public PetitionUser(){

    }

    public PetitionUser(String userId, String userFullName, boolean isTeacher ,int petitionStatus){
        this.userId = userId;
        this.userFullName = userFullName;
        this.isTeacher = isTeacher;
        this.petitionStatus = petitionStatus;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public int getPetitionStatus() {
        return petitionStatus;
    }
}
