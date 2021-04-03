package com.elcazadordebaterias.coordinapp.utils;


public class CreateGroupDialogSpinnerItem {
    private String participantName;
    private String participantId;
    private boolean isSelected;
    private boolean isTeacher;

    public CreateGroupDialogSpinnerItem(String participantName, String participantId, boolean isSelected, boolean isTeacher){
        this.participantName = participantName;
        this.participantId = participantId;
        this.isSelected = isSelected;
        this.isTeacher = isTeacher;
    }

    public String getParticipantName() {
        return participantName;
    }

    public String getParticipantId(){
        return participantId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean isTeacher(){
        return isTeacher;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

}
