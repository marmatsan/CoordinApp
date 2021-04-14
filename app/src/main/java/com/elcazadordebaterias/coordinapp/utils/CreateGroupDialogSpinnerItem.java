package com.elcazadordebaterias.coordinapp.utils;

/**
 * Reprsents the custom spinner item used in {@link CreateGroupDialog} and {@link com.elcazadordebaterias.coordinapp.adapters.CreateGroupDialogParticipantsAdapter}
 *
 * @see CreateGroupDialog
 * @see com.elcazadordebaterias.coordinapp.adapters.CreateGroupDialogParticipantsAdapter
 * @author Martín Mateos Sánchez
 */
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
