package com.elcazadordebaterias.coordinapp.utils.cards;

import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.StorageFile;
import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.Date;

public class ChatMessageCard {

    private String messageTitle;
    private String senderId;
    private String message;
    private Timestamp date;
    private String stringRepD;
    private StorageFile fileRef;
    private String senderName;
    private String fileName;

    public ChatMessageCard(){

    }

    public ChatMessageCard(String messageTitle, String senderId, String message, Timestamp date, String stringRepD, StorageFile fileRef, String senderName, String fileName) {
        this.messageTitle = messageTitle;
        this.senderId = senderId;
        this.message = message;
        this.date = date;
        this.stringRepD = stringRepD;
        this.fileRef = fileRef;
        this.senderName = senderName;
        this.fileName = fileName;
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

    public Timestamp getDate() {
        return date;
    }

    public String getStringRepD() {
        return stringRepD;
    }

    public StorageFile getFileRef() {
        return fileRef;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getFileName() {
        return fileName;
    }
}
