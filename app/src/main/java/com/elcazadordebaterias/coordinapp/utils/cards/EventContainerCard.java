package com.elcazadordebaterias.coordinapp.utils.cards;

import java.util.ArrayList;

public class EventContainerCard {

    private String groupName;
    private ArrayList<EventCard> eventsList;

    public EventContainerCard(String groupName, ArrayList<EventCard> eventsList) {
        this.groupName = groupName;
        this.eventsList = eventsList;
    }

    public String getGroupName() {
        return groupName;
    }

    public ArrayList<EventCard> getEventsList() {
        return eventsList;
    }
}
