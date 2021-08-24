package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments;

import com.elcazadordebaterias.coordinapp.utils.customdatamodels.InteractivityCardType;

import java.util.ArrayList;

public class MultichoiceCardDocument {

    private String title;
    private boolean hasToBeEvaluated;
    private boolean hasTeacherVisibility;
    private ArrayList<Question> questionsList;
    private ArrayList<MultichoiceCardStudentData> studentsData;

    public MultichoiceCardDocument() {

    }

    public MultichoiceCardDocument(String title, boolean hasToBeEvaluated, ArrayList<Question> questionsList, ArrayList<String> studentsIDs) {
        this.title = title;
        this.hasToBeEvaluated = hasToBeEvaluated;
        this.questionsList = questionsList;
        this.hasTeacherVisibility = true;

        ArrayList<MultichoiceCardStudentData> studentsData = new ArrayList<MultichoiceCardStudentData>();
        for (String studentID : studentsIDs) {
            studentsData.add(new MultichoiceCardStudentData(studentID));
        }
        this.studentsData = studentsData;

    }

    public String getTitle() {
        return title;
    }

    public boolean getHasToBeEvaluated() {
        return hasToBeEvaluated;
    }

    public boolean getHasTeacherVisibility() {
        return hasTeacherVisibility;
    }

    public ArrayList<Question> getQuestionsList() {
        return questionsList;
    }

    public ArrayList<MultichoiceCardStudentData> getStudentsData() {
        return studentsData;
    }

    public int getCardType() {
        return InteractivityCardType.TYPE_CHOICES;
    }


    public static class MultichoiceCardStudentData {
        private String studentID;
        private int questionRespondedIdentifier;
        private int mark;

        public MultichoiceCardStudentData() {

        }

        public MultichoiceCardStudentData(String studentID) {
            this.studentID = studentID;
            this.questionRespondedIdentifier = -1;
        }

        public String getStudentID() {
            return studentID;
        }

        public int getQuestionRespondedIdentifier() {
            return questionRespondedIdentifier;
        }

        public void setQuestionRespondedIdentifier(int questionRespondedIdentifier) {
            this.questionRespondedIdentifier = questionRespondedIdentifier;
        }

        public int getMark() {
            return mark;
        }

        public void setMark(int mark) {
            this.mark = mark;
        }
    }

    public static class Question {
        private String questionTitle;
        private int questionIdentifier;
        private boolean hasCorrectAnswer;

        public Question() {

        }

        public Question(String questionTitle, int questionIdentifier) {
            this.questionTitle = questionTitle;
            this.questionIdentifier = questionIdentifier;
        }

        public String getQuestionTitle() {
            return questionTitle;
        }

        public int getQuestionIdentifier() {
            return questionIdentifier;
        }

        public boolean getHasCorrectAnswer() {
            return hasCorrectAnswer;
        }

        public void setHasCorrectAnswer(boolean hasCorrectAnswer) {
            this.hasCorrectAnswer = hasCorrectAnswer;
        }
    }

}
