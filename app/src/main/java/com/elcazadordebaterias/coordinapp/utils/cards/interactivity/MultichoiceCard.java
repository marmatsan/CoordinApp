package com.elcazadordebaterias.coordinapp.utils.cards.interactivity;

import java.util.ArrayList;

public class MultichoiceCard extends InteractivityCard {

    private ArrayList<String> questions;

    public MultichoiceCard(String cardTitle, ArrayList<String> questions) {
        super(cardTitle);
        this.questions = questions;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }
}
