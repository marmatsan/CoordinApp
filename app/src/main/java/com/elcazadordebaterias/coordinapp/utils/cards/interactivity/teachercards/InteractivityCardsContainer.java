package com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class InteractivityCardsContainer {

    private String name;
    private ArrayList<InteractivityCard> interactivityCardsList;

    private int evaluableGroupalTextCards;
    private int totalGroupalTextMark;

    QuerySnapshot allInteractivityDocumentsSnapshots;

    public InteractivityCardsContainer(String name, ArrayList<InteractivityCard> interactivityCardsList, QuerySnapshot allInteractivityDocumentsSnapshots) {
        this.name = name;
        this.interactivityCardsList = interactivityCardsList;
        this.allInteractivityDocumentsSnapshots = allInteractivityDocumentsSnapshots;
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

    public QuerySnapshot getAllInteractivityDocumentsSnapshots() {
        return allInteractivityDocumentsSnapshots;
    }
}
