package com.elcazadordebaterias.coordinapp.utils.cards.groups;

import java.util.ArrayList;
import java.util.Collections;

public class GroupsContainerCard {

    private String name;
    private String spokerName;
    private String spokerID;
    private ArrayList<String> participantsNames;

    private ArrayList<GroupCard> groupList;

    public GroupsContainerCard(String name, String spokerName, String spokerID, ArrayList<String> participantNames, ArrayList<GroupCard> groupList) {
        this.name = name;
        this.spokerName = spokerName;
        this.spokerID = spokerID;
        this.participantsNames = participantNames;
        this.groupList = groupList;
    }

    public String getName() {
        return name;
    }

    public ArrayList<GroupCard> getGroupList() {
        return groupList;
    }

    public String getSpokerName() {
        return spokerName;
    }

    public String getSpokerID() {
        return spokerID;
    }

    public ArrayList<String> getParticipantsNames() {
        Collections.sort(participantsNames);
        return participantsNames;
    }
}
