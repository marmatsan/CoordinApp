package com.elcazadordebaterias.coordinapp.utils.customdatamodels;

/**
 * Used in the listview when we select the users
 *
 */
public class SelectParticipantItem {

    private final String participantName;
    private final String participantId;
    private boolean isSelected;
    private boolean isSpoker;

    public SelectParticipantItem(String participantName, String participantId) {
        this.participantName = participantName;
        this.participantId = participantId;
        this.isSelected = false;
        this.isSpoker = false;
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

    public boolean isSpoker() {
        return isSpoker;
    }

    public void setSpoker(boolean spoker) {
        isSpoker = spoker;
    }
}
