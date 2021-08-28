package com.elcazadordebaterias.coordinapp.utils.cards;

public class EventCard {

    private String eventName;
    private String eventMessage;
    private String eventPlace;

    public EventCard(String eventName, String eventMessage, String eventPlace) {
        this.eventName = eventName;
        this.eventMessage = eventMessage;
        this.eventPlace = eventPlace;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventMessage() {
        return eventMessage;
    }

    public String getEventPlace() {
        return eventPlace;
    }
}
