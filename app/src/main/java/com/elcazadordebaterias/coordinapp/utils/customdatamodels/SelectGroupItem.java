package com.elcazadordebaterias.coordinapp.utils.customdatamodels;

public class SelectGroupItem {

    private final String groupName;
    private final String groupID;
    private boolean isSelected;

    public SelectGroupItem(String groupName, String groupID) {
        this.groupName = groupName;
        this.groupID = groupID;
        this.isSelected = false;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupID() {
        return groupID;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
