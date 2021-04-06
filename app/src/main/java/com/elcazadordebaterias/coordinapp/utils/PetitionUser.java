package com.elcazadordebaterias.coordinapp.utils;

public class PetitionUser {

    private String userId;
    private String userFullName;
    private boolean userAsTeacher;
    private int petitionStatus;

    public PetitionUser(){

    }

    public PetitionUser(String userId, String userFullName, boolean userAsTeacher ,int petitionStatus){
        this.userId = userId;
        this.userFullName = userFullName;
        this.userAsTeacher = userAsTeacher;
        this.petitionStatus = petitionStatus;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public boolean getUserAsTeacher() {
        return userAsTeacher;
    }

    public int getPetitionStatus() {
        return petitionStatus;
    }

    public void setPetitionStatus(int petitionStatus){
        this.petitionStatus = petitionStatus;
    }
}
