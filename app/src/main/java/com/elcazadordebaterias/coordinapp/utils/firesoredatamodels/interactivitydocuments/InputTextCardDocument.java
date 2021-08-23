package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments;

import com.elcazadordebaterias.coordinapp.utils.customdatamodels.InteractivityCardType;

import java.util.ArrayList;

public class InputTextCardDocument {

    private String title;
    private boolean hasAllActivitiesGraded;
    private ArrayList<InputTextCardStudentData> studentsData;

    public InputTextCardDocument() {

    }

    public InputTextCardDocument(String title, ArrayList<String> studentsIDs) {
        this.title = title;
        ArrayList<InputTextCardStudentData> studentsData = new ArrayList<InputTextCardStudentData>();

        for (String studentID : studentsIDs) {
            studentsData.add(new InputTextCardStudentData(studentID));
        }

        this.hasAllActivitiesGraded = false;
        this.studentsData = studentsData;
    }

    public String getTitle() {
        return title;
    }

    public boolean getHasAllActivitiesGraded() {
        return hasAllActivitiesGraded;
    }

    public int getCardType() {
        return InteractivityCardType.TYPE_INPUTTEXT;
    }

    public ArrayList<InputTextCardStudentData> getStudentsData() {
        return studentsData;
    }


    public static class InputTextCardStudentData {
        private String studentID;
        private float mark;
        private String response;

        public InputTextCardStudentData() {

        }

        public InputTextCardStudentData(String studentID) {
            this.studentID = studentID;
        }

        public String getStudentID() {
            return studentID;
        }

        public float getMark() {
            return mark;
        }

        public String getResponse() {
            return response;
        }

        public void setStudentID(String studentID) {
            this.studentID = studentID;
        }

        public void setMark(float mark) {
            this.mark = mark;
        }

        public void setResponse(String response) {
            this.response = response;
        }

    }

}
