package com.elcazadordebaterias.coordinapp.utils.customdatamodels;

/**
 * Used in the listview when we select the users
 *
 */
public class SelectParticipantItem {

    private String participantName;
    private String participantId;
    private boolean isSelected;

    public SelectParticipantItem(String participantName, String participantId, boolean isSelected) {
        this.participantName = participantName;
        this.participantId = participantId;
        this.isSelected = isSelected;
    }

    public String getParticipantName() {
        return participantName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
