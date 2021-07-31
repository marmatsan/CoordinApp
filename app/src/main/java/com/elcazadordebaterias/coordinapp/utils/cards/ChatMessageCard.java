package com.elcazadordebaterias.coordinapp.utils.cards;

import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.StorageFileReference;

import java.util.Date;

public class ChatMessageCard {

    private String messageTitle;
    private String senderId;
    private String message;
    private Date date;
    private StorageFileReference fileRef;

    public ChatMessageCard(){

    }

    public ChatMessageCard(String messageTitle, String senderId, String message, Date date, StorageFileReference fileRef) {
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

    public StorageFileReference getFileRef() {
        return fileRef;
    }
}
