package com.elcazadordebaterias.coordinapp.utils.cards.interactivity.studentcards;

public class InteractivityCard {

    private String cardTitle;
    private String studentID;

    public InteractivityCard() {

    }

    public InteractivityCard(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public InteractivityCard(String cardTitle, String studentID){
        this.cardTitle = cardTitle;
        this.studentID = studentID;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

}
