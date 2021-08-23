package com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards;

import com.google.firebase.firestore.DocumentSnapshot;

public class InputTextCard extends InteractivityCard {

    private String studentID;
    private String studentAnswer;
    private DocumentSnapshot documentSnapshot;

    public InputTextCard() {

    }

    public InputTextCard(String cardTitle, String studentID, String studentAnswer, DocumentSnapshot documentSnapshot) {
        super(cardTitle);
        this.studentID = studentID;
        this.studentAnswer = studentAnswer;
        this.documentSnapshot = documentSnapshot;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public DocumentSnapshot getDocumentSnapshot() {
        return documentSnapshot;
    }
}
