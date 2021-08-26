package com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards;

import java.util.ArrayList;

public class InteractivityCardsContainer {

    private String name;
    private ArrayList<InteractivityCard> interactivityCardsList;

    private int evaluableGroupalTextCards;
    private int totalGroupalTextMark;



    public InteractivityCardsContainer(String name, ArrayList<InteractivityCard> interactivityCardsList) {
        this.name = name;
        this.interactivityCardsList = interactivityCardsList;
    }

    public String getName() {
        return name;
    }

    public ArrayList<InteractivityCard> getInteractivityCardsList() {
        return interactivityCardsList;
    }

    public int getEvaluableGroupalTextCards() {
        return evaluableGroupalTextCards;
    }

    public int getAverageGroupalTextMark() {

        int averageGroupalTextMark = 0;

        if (evaluableGroupalTextCards != 0){
            averageGroupalTextMark = totalGroupalTextMark/evaluableGroupalTextCards;
        }

        return averageGroupalTextMark;
    }

    public void setEvaluableGroupalTextCards(int evaluableGroupalTextCards) {
        this.evaluableGroupalTextCards = evaluableGroupalTextCards;
    }

    public void setTotalGroupalTextMark(int totalGroupalTextMark) {
        this.totalGroupalTextMark = totalGroupalTextMark;
    }
}
