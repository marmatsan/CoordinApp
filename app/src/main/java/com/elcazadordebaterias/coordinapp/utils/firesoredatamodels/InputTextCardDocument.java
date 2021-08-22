package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;

import com.elcazadordebaterias.coordinapp.utils.customdatamodels.InteractivityCardType;

import java.util.ArrayList;

public class InputTextCardDocument {

    private final int cardType = InteractivityCardType.TYPE_INPUTTEXT;
    private String title;
    private ArrayList<InputTextCardStudentData> studentsData;

    public InputTextCardDocument(String title, ArrayList<String> studentsIDs) {
        this.title = title;
        ArrayList<InputTextCardStudentData> studentsData = new ArrayList<InputTextCardStudentData>();

        for (String studentID : studentsIDs) {
            studentsData.add(new InputTextCardStudentData(studentID));
        }

        this.studentsData = studentsData;
    }

    public int getCardType() {
        return cardType;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<InputTextCardStudentData> getStudentsData() {
        return studentsData;
    }

    private static class InputTextCardStudentData {
        private String studentID;
        private int mark;
        private String studentResponse;
        private boolean responded;

        public InputTextCardStudentData(String studentID) {
            this.studentID = studentID;
        }

        public String getStudentID() {
            return studentID;
        }

        public int getMark() {
            return mark;
        }

        public String getStudentResponse() {
            return studentResponse;
        }

        public boolean getHasResponded() {
            return responded;
        }

        public void setStudentID(String studentID) {
            this.studentID = studentID;
        }

        public void setMark(int mark) {
            this.mark = mark;
        }

        public void setStudentResponse(String studentResponse) {
            this.studentResponse = studentResponse;
        }

        public void setResponded(boolean responded) {
            this.responded = responded;
        }
    }

}
