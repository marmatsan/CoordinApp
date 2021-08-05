package com.elcazadordebaterias.coordinapp.utils.cards;

import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.StorageFile;

import java.util.Date;

public class ChatMessageCard {

    private String messageTitle;
    private String senderId;
    private String message;
    private Date date;
    private StorageFile fileRef;

    public ChatMessageCard(){

    }

    public ChatMessageCard(String messageTitle, String senderId, String message, Date date, StorageFile fileRef) {
        this.messageTitle = messageTitle;
        this.senderId = senderId;
        this.message = message;
        this.date = date;
        this.fileRef = fileRef;
    }

    public String getMessageTitle() {
        return messageTitle;
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

    public StorageFile getFileRef() {
        return fileRef;
    }
}
