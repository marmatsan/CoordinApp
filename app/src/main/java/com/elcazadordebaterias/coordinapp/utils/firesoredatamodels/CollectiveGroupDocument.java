package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;

import java.util.ArrayList;

public class CollectiveGroupDocument {

    private String name;
    private ArrayList<String> allParticipantsIDs;
    private ArrayList<Group> groups;

    public CollectiveGroupDocument(){

    }

    public CollectiveGroupDocument(String name, ArrayList<String> allParticipantsIDs, ArrayList<Group> groups){
        this.name = name;
        this.allParticipantsIDs = allParticipantsIDs;
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getAllParticipantsIDs() {
        return allParticipantsIDs;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }
}
