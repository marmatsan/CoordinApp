package com.elcazadordebaterias.coordinapp.utils.cards;

import java.util.Date;

public class ChatMessageCard {

    private String senderName;
    private String senderId;
    private String message;
    private Date date;
    public ChatMessageCard(){

    }

    public ChatMessageCard(String senderName, String senderId, String message, Date date) {
        this.senderName = senderName;
        this.senderId = senderId;
        this.message = message;
        this.date = date;
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

    public Date getDate() {
        return date;
    }
}
