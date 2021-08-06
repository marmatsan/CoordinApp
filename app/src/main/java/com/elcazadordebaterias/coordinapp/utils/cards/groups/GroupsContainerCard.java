package com.elcazadordebaterias.coordinapp.utils.cards.groups;

import java.util.ArrayList;

public class GroupsContainerCard {

    private String name;
    private ArrayList<GroupCard> groupList;

    public GroupsContainerCard(String name, ArrayList<GroupCard> groupList) {
        this.name = name;
        this.groupList = groupList;
    }

    public String getName() {
        return name;
    }

    public ArrayList<GroupCard> getGroupList() {
        return groupList;
    }

}
