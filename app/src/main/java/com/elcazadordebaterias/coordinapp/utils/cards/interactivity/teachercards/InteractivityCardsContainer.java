package com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class InteractivityCardsContainer {

    private String name;
    private ArrayList<InteractivityCard> interactivityCardsList;

    private ArrayList<Double> statistics;

    QuerySnapshot allInteractivityDocumentsSnapshots;

    public InteractivityCardsContainer(String name, ArrayList<InteractivityCard> interactivityCardsList, QuerySnapshot allInteractivityDocumentsSnapshots, ArrayList<Double> statistics) {
        this.name = name;
        this.interactivityCardsList = interactivityCardsList;
        this.allInteractivityDocumentsSnapshots = allInteractivityDocumentsSnapshots;
        this.statistics = statistics;
    }

    public String getName() {
        return name;
    }

    public ArrayList<InteractivityCard> getInteractivityCardsList() {
        return interactivityCardsList;
    }

    public QuerySnapshot getAllInteractivityDocumentsSnapshots() {
        return allInteractivityDocumentsSnapshots;
    }

    public ArrayList<Double> getStatistics() {
        return statistics;
    }

}
