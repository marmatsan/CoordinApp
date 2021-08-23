package com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards;

import java.util.ArrayList;

public class GroupsInteractivityCardsContainer {

    private String name;
    private ArrayList<InteractivityCard> interactivityCardsList;

    public GroupsInteractivityCardsContainer(String name, ArrayList<InteractivityCard> interactivityCardsList) {
        this.name = name;
        this.interactivityCardsList = interactivityCardsList;
    }

    public String getName() {
        return name;
    }

    public ArrayList<InteractivityCard> getInteractivityCardsList() {
        return interactivityCardsList;
    }

    public boolean allCardsInvisible() {
        boolean allCardsInvisible = true;
        for (InteractivityCard card : interactivityCardsList) {
            if (card.getHasTeacherVisibility()) {
                allCardsInvisible = false;
                break;
            }
        }
        return allCardsInvisible;
    }

    public boolean allCardsResponded(){
        boolean allCardsResponded = false;

        ArrayList<Stu>

        return allCardsResponded;
    }

}
