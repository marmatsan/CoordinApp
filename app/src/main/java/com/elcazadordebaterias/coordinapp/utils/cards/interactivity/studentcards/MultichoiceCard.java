package com.elcazadordebaterias.coordinapp.utils.cards.interactivity.studentcards;

import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.MultichoiceCardDocument;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class MultichoiceCard extends InteractivityCard {

    private ArrayList<MultichoiceCardDocument.Question> questionsList;
    private DocumentSnapshot documentSnapshot;

    public MultichoiceCard() {

    }

    public MultichoiceCard(String cardTitle, String studentID, ArrayList<MultichoiceCardDocument.Question> questionsList, DocumentSnapshot documentSnapshot) {
        super(cardTitle, studentID);
        this.questionsList = questionsList;
        this.documentSnapshot = documentSnapshot;
    }

    public ArrayList<MultichoiceCardDocument.Question> getQuestionsList() {
        return questionsList;
    }

    public DocumentSnapshot getDocumentSnapshot() {
        return documentSnapshot;
    }
}
