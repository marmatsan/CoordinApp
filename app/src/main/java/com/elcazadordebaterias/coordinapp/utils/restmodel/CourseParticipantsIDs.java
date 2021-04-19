package com.elcazadordebaterias.coordinapp.utils.restmodel;

import java.util.ArrayList;

public class CourseParticipantsIDs {

    private ArrayList<String> allParticipantsIDs;

    public CourseParticipantsIDs() {
        super();
    }

    public CourseParticipantsIDs(ArrayList<String> allParticipantsIDs) {
        this.allParticipantsIDs = allParticipantsIDs;
    }

    public ArrayList<String> getAllParticipantsIDs() {
        return this.allParticipantsIDs;
    }

    public void setAllParticipantsIDs(ArrayList<String> allParticipantsIDs) {
        this.allParticipantsIDs = allParticipantsIDs;
    }

}