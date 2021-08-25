package com.elcazadordebaterias.coordinapp.utils.cards.interactivity.studentcards;

public class StandByCard extends InteractivityCard {

    private String spokerName;

    public StandByCard(String cardTitle, String studentID, String spokerName) {
        super(cardTitle, studentID);
        this.spokerName = spokerName;
    }

    public String getSpokerName() {
        return spokerName;
    }

}
