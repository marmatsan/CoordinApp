package com.elcazadordebaterias.coordinapp.utils.cards.interactivity.studentcards;

import java.util.ArrayList;

public class MultichoiceCard extends InteractivityCard {

    private ArrayList<String> questions;

    public MultichoiceCard() {

    }

    public MultichoiceCard(String cardTitle, String studentID, ArrayList<String> questions) {
        super(cardTitle, studentID);
        this.questions = questions;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }
}
