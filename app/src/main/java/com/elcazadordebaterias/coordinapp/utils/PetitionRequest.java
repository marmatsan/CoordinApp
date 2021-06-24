package com.elcazadordebaterias.coordinapp.utils;

import java.util.ArrayList;

public class PetitionRequest {

    private String course;
    private String subject;
    private String requesterId;
    private String requesterName;
    private String teacherId;
    private String teacherName;
    private ArrayList<PetitionUser> petitionUsersList;
    private ArrayList<String> petitionUsersIds;

    public PetitionRequest(){

    }

    public PetitionRequest(String course, String subject, String requesterId, String requesterName, String teacherId, String teacherName, ArrayList<String> petitionUsersIds, ArrayList<PetitionUser> petitionUsersList){
        this.course = course;
        this.subject = subject;
        this.requesterId = requesterId;
        this.requesterName = requesterName;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
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

    public String getTeacherId() {
        return teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public ArrayList<String> getPetitionUsersIds() {
        return petitionUsersIds;
    }

    public ArrayList<PetitionUser> getPetitionUsersList() {
        return petitionUsersList;
    }



}
