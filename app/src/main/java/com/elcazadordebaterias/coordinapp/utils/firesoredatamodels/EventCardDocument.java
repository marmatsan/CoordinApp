package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;

public class EventCardDocument {

    private String eventTile;
    private String eventDescription;
    private String eventPlace;
    private String eventDate;
    private boolean sentByTeacher;
    private String senderID;

    public EventCardDocument() {

    }

    public EventCardDocument(String eventTile, String eventDescription, String eventPlace, String eventDate, boolean sentByTeacher, String senderID) {
        this.eventTile = eventTile;
        this.eventDescription = eventDescription;
        this.eventPlace = eventPlace;
        this.eventDate = eventDate;
        this.sentByTeacher = sentByTeacher;
        this.senderID = senderID;
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

    public String getEventDate() {
        return eventDate;
    }

    public String getSenderID() {
        return senderID;
    }

    public boolean getSentByTeacher() {
        return sentByTeacher;
    }
}
