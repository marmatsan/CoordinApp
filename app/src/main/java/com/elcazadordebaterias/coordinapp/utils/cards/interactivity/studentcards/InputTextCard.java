package com.elcazadordebaterias.coordinapp.utils.cards.interactivity.studentcards;

import com.google.firebase.firestore.DocumentSnapshot;

public class InputTextCard extends InteractivityCard {

    private DocumentSnapshot documentSnapshot;

    public InputTextCard() {

    }

    public InputTextCard(String cardTitle, String studentID, DocumentSnapshot documentSnapshot) {
        super(cardTitle, studentID);
        this.documentSnapshot = documentSnapshot;
    }

    public DocumentSnapshot getDocumentSnapshot() {
        return documentSnapshot;
    }
}
