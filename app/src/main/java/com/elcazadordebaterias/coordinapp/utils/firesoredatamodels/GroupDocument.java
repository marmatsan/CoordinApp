package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;

import java.util.ArrayList;

public class GroupDocument {

    private String name;
    private ArrayList<String> allParticipantsIDs;

    public GroupDocument(){

    }

    public GroupDocument(String name, ArrayList<String> allParticipantsIDs){
        this.name = name;
        this.allParticipantsIDs = allParticipantsIDs;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getAllParticipantsIDs() {
        return allParticipantsIDs;
    }
}
