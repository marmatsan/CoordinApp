package com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards;

import com.google.firebase.firestore.DocumentSnapshot;

public class InputTextCard extends InteractivityCard {

    private DocumentSnapshot documentSnapshot;

    public InputTextCard() {

    }

    public InputTextCard(String cardTitle, DocumentSnapshot documentSnapshot) {
        super(cardTitle);
        this.documentSnapshot = documentSnapshot;
    }

    public DocumentSnapshot getDocumentSnapshot() {
        return documentSnapshot;
    }
}
