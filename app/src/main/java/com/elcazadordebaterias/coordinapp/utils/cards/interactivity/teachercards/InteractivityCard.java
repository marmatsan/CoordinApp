package com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards;

public class InteractivityCard {

    private String cardTitle;
    private boolean hasTeacherVisibility;

    public InteractivityCard() {

    }

    public InteractivityCard(String cardTitle){
        this.cardTitle = cardTitle;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public boolean getHasTeacherVisibility() {
        return hasTeacherVisibility;
    }

    public void setHasTeacherVisibility(boolean hasTeacherVisibility) {
        this.hasTeacherVisibility = hasTeacherVisibility;
    }
}
