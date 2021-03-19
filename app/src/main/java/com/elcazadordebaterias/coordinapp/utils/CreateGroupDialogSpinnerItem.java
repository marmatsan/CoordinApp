package com.elcazadordebaterias.coordinapp.utils;

public class CreateGroupDialogSpinnerItem {
    private String mParticipantName;
    private boolean mSelected;

    public CreateGroupDialogSpinnerItem(String participantName, boolean selected){
        this.mParticipantName = participantName;
        this.mSelected = selected;
    }

    public String getParticipantName() {
        return mParticipantName;
    }

    public void setParticipantName(String participantName) {
        this.mParticipantName = participantName;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        this.mSelected = selected;
    }
}
