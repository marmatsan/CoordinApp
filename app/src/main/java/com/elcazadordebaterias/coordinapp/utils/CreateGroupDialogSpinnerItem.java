package com.elcazadordebaterias.coordinapp.utils;


public class CreateGroupDialogSpinnerItem {
    private boolean isTeacher;
    private String mParticipantName;
    private boolean mSelected;

    public CreateGroupDialogSpinnerItem(String participantName, boolean selected, boolean isTeacher){
        this.mParticipantName = participantName;
        this.mSelected = selected;
        this.isTeacher = isTeacher;
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
