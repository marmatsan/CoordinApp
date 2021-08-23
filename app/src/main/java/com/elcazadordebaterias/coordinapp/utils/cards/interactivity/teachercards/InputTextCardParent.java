package com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class InputTextCardParent extends InteractivityCard {

    private String averageGrade;
    private String studentsThatHaveNotAnswered;
    private ArrayList<InputTextCardChild> inputTextCardChildList;
    private DocumentSnapshot documentSnapshot;

    public InputTextCardParent(String cardTitle, ArrayList<InputTextCardChild> inputTextCardChildList, DocumentSnapshot documentSnapshot) {
        super(cardTitle);
        this.inputTextCardChildList = inputTextCardChildList;
        this.documentSnapshot = documentSnapshot;
    }

    public String getAverageGrade() {
        return averageGrade;
    }

    public String getStudentsThatHaveNotAnswered() {
        return studentsThatHaveNotAnswered;
    }

    public ArrayList<InputTextCardChild> getInputTextCardChildList() {
        return inputTextCardChildList;
    }

    public DocumentSnapshot getDocumentSnapshot() {
        return documentSnapshot;
    }

    public void setAverageGrade(String averageGrade) {
        this.averageGrade = averageGrade;
    }

    public void setStudentsThatHaveNotAnswered(String studentsThatHaveNotAnswered) {
        this.studentsThatHaveNotAnswered = studentsThatHaveNotAnswered;
    }

    public static class InputTextCardChild {

        private String studentID;
        private String response;
        private DocumentSnapshot documentSnapshot;

        public InputTextCardChild() {

        }

        public InputTextCardChild(String studentID, String response, DocumentSnapshot documentSnapshot) {
            this.studentID = studentID;
            this.response = response;
            this.documentSnapshot = documentSnapshot;
        }

        public String getStudentID() {
            return studentID;
        }

        public String getResponse() {
            return response;
        }

        public DocumentSnapshot getDocumentSnapshot() {
            return documentSnapshot;
        }
    }

}
