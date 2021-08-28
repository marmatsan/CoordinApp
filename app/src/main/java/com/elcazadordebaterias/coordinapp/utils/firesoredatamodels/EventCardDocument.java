package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;

public class EventCardDocument {

    private String eventTile;
    private String eventDescription;
    private String eventPlace;

    public EventCardDocument() {

    }

    public EventCardDocument(String eventTile, String eventDescription, String eventPlace) {
        this.eventTile = eventTile;
        this.eventDescription = eventDescription;
        this.eventPlace = eventPlace;
    }

    public String getEventTile() {
        return eventTile;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getEventPlace() {
        return eventPlace;
    }
}
