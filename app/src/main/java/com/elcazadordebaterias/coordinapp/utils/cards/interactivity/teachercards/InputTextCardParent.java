package com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards;


import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.InputTextCardDocument;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class InputTextCardParent extends InteractivityCard {

    private final ArrayList<InputTextCardChild> inputTextCardChildList;
    private final DocumentSnapshot documentSnapshot;

    public InputTextCardParent(DocumentSnapshot documentSnapshot) {
        super(documentSnapshot.toObject(InputTextCardDocument.class).getTitle());
        this.inputTextCardChildList = new ArrayList<InputTextCardParent.InputTextCardChild>();
        this.documentSnapshot = documentSnapshot;

        populateChildsList();
    }

    private void populateChildsList() {

        for (InputTextCardDocument.InputTextCardStudentData studentData : getInputTextCardDocument().getStudentsData()) {
            if (studentData.getResponse() != null) {
                InputTextCardParent.InputTextCardChild childInputTextCard =
                        new InputTextCardParent.InputTextCardChild(
                                studentData.getStudentID(),
                                studentData.getResponse(),
                                getInputTextCardDocument(),
                                documentSnapshot.getReference()
                        );
                inputTextCardChildList.add(childInputTextCard);
            }
        }

    }

    public ArrayList<InputTextCardChild> getInputTextCardChildList() {
        return inputTextCardChildList;
    }

    public DocumentSnapshot getDocumentSnapshot() {
        return documentSnapshot;
    }

    public InputTextCardDocument getInputTextCardDocument() {
        return documentSnapshot.toObject(InputTextCardDocument.class);
    }

    public boolean getHasTeacherVisibility() {
        return getInputTextCardDocument().getHasTeacherVisibility();
    }

    public static class InputTextCardChild {

        private String studentID;
        private String response;
        private InputTextCardDocument inputTextCardDocument;
        private DocumentReference documentReference;

        public InputTextCardChild() {

        }

        public InputTextCardChild(String studentID, String response, InputTextCardDocument inputTextCardDocument, DocumentReference documentReference) {
            this.studentID = studentID;
            this.response = response;
            this.inputTextCardDocument = inputTextCardDocument;
            this.documentReference = documentReference;
        }

        public String getStudentID() {
            return studentID;
        }

        public String getResponse() {
            return response;
        }

        public InputTextCardDocument getInputTextCardDocument() {
            return inputTextCardDocument;
        }

        public DocumentReference getDocumentReference() {
            return documentReference;
        }
    }

}
