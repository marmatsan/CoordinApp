package com.elcazadordebaterias.coordinapp.utils;


public class CreateGroupDialogSpinnerItem {
    private boolean mIsTeacher;
    private String mParticipantName;
    private boolean mSelected;
    private String mParticipantId;

    public CreateGroupDialogSpinnerItem(String participantName, String participantId, boolean selected, boolean isTeacher){
        this.mParticipantId = participantId;
        this.mParticipantName = participantName;
        this.mSelected = selected;
        this.mIsTeacher = isTeacher;
    }

    public String getParticipantName() {
        return mParticipantName;
    }

    public String getParticipantId(){
        return mParticipantId;
    }

    public boolean isTeacher(){
        return mIsTeacher;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        this.mSelected = selected;
    }
}
