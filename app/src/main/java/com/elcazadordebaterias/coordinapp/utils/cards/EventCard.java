package com.elcazadordebaterias.coordinapp.utils.cards;

import com.google.firebase.firestore.DocumentSnapshot;

public class EventCard {

    private String eventName;
    private String eventMessage;
    private String eventPlace;
    private DocumentSnapshot documentSnapshot;
    private String senderID;
    private boolean sentByTeacher;

    public EventCard(String eventName, String eventMessage, String eventPlace, DocumentSnapshot documentSnapshot, String senderID, boolean sentByTeacher) {
        this.eventName = eventName;
        this.eventMessage = eventMessage;
        this.eventPlace = eventPlace;
        this.documentSnapshot = documentSnapshot;
        this.senderID = senderID;
        this.sentByTeacher = sentByTeacher;
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

    public DocumentSnapshot getDocumentSnapshot() {
        return documentSnapshot;
    }

    public String getSenderID() {
        return senderID;
    }

    public boolean isSentByTeacher() {
        return sentByTeacher;
    }
}
