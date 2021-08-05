package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;

import java.util.ArrayList;

public class GroupDocument {

    private ArrayList<String> allParticipantsIDs;

    public GroupDocument(){

    }

    public GroupDocument(ArrayList<String> allParticipantsIDs){
        this.allParticipantsIDs = allParticipantsIDs;
    }

    public ArrayList<String> getAllParticipantsIDs() {
        return allParticipantsIDs;
    }
}
