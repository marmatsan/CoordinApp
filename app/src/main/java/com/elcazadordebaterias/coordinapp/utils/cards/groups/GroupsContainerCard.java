package com.elcazadordebaterias.coordinapp.utils.cards.groups;

import java.util.ArrayList;

public class GroupsContainerCard {

    private String name;
    private String spokerName;
    private ArrayList<String> participantsNames;

    private ArrayList<GroupCard> groupList;

    public GroupsContainerCard(String name, String spokerName, ArrayList<String> participantNames, ArrayList<GroupCard> groupList) {
        this.name = name;
        this.spokerName = spokerName;
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

    public ArrayList<String> getParticipantsNames() {
        return participantsNames;
    }
}
