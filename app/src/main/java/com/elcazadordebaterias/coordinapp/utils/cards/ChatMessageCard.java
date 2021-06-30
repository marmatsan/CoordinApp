package com.elcazadordebaterias.coordinapp.utils.cards;

public class ChatMessageCard {

    private String senderName;
    private String senderId;
    private String message;

    public ChatMessageCard(){

    }

    public ChatMessageCard(String senderName, String senderId, String message) {
        this.senderName = senderName;
        this.senderId = senderId;
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

}