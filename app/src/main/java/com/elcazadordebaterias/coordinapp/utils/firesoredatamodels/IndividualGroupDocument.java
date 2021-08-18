package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;

import java.util.ArrayList;

public class IndividualGroupDocument {

    private String name;
    private ArrayList<String> allParticipantsIDs;
    private Group group;
    private boolean hasVisibility;

    public IndividualGroupDocument() {

    }

    public IndividualGroupDocument(String name, ArrayList<String> allParticipantsIDs, Group group) {
        this.name = name;
        this.allParticipantsIDs = allParticipantsIDs;
        this.group = group;
        this.hasVisibility = false;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getAllParticipantsIDs() {
        return allParticipantsIDs;
    }

    public Group getGroup() {
        return group;
    }

    public boolean getHasVisibility() {
        return hasVisibility;
    }

    public void setVisible(boolean hasVisibility) {
        this.hasVisibility = hasVisibility;
    }
}
