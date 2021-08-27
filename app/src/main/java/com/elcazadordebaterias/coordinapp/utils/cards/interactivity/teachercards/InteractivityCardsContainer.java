package com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class InteractivityCardsContainer {

    private String name;
    private ArrayList<InteractivityCard> interactivityCardsList;

    private HashMap<String, Double> statistics;

    QuerySnapshot allInteractivityDocumentsSnapshots;

    public InteractivityCardsContainer(String name, ArrayList<InteractivityCard> interactivityCardsList, QuerySnapshot allInteractivityDocumentsSnapshots, HashMap<String, Double> statistics) {
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

    public HashMap<String, Double> getStatistics() {
        return statistics;
    }

}
