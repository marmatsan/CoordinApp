package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments;

import com.elcazadordebaterias.coordinapp.utils.customdatamodels.InteractivityCardType;

import java.util.ArrayList;

public class InputTextCardDocument {

    private String title;
    private boolean hasTeacherVisibility;
    private ArrayList<InputTextCardStudentData> studentsData;

    public InputTextCardDocument() {

    }

    public InputTextCardDocument(String title, ArrayList<String> studentsIDs) {
        this.title = title;
        ArrayList<InputTextCardStudentData> studentsData = new ArrayList<InputTextCardStudentData>();

        for (String studentID : studentsIDs) {
            studentsData.add(new InputTextCardStudentData(studentID));
        }

        this.hasTeacherVisibility = false;
        this.studentsData = studentsData;
    }

    public boolean getHasTeacherVisibility() {
        return hasTeacherVisibility;
    }

    public int getCardType() {
        return InteractivityCardType.TYPE_INPUTTEXT;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<InputTextCardStudentData> getStudentsData() {
        return studentsData;
    }

    public static class InputTextCardStudentData {
        private String studentID;
        private float mark;
        private String studentResponse;
        private boolean hasMarkSet;
        private boolean hasResponded;

        public InputTextCardStudentData() {

        }

        public InputTextCardStudentData(String studentID) {
            this.studentID = studentID;
            this.hasMarkSet = false;
        }

        public String getStudentID() {
            return studentID;
        }

        public float getMark() {
            return mark;
        }

        public String getStudentResponse() {
            return studentResponse;
        }

        public boolean getHasMarkSet(){
            return hasMarkSet;
        }

        public boolean getHasResponded() {
            return hasResponded;
        }

        public void setStudentID(String studentID) {
            this.studentID = studentID;
        }

        public void setMark(float mark) {
            this.mark = mark;
        }

        public void setStudentResponse(String studentResponse) {
            this.studentResponse = studentResponse;
        }

        public void setHasMarkSet(boolean hasMarkSet) {
            this.hasMarkSet = hasMarkSet;
        }

        public void sethasResponded(boolean responded) {
            this.hasResponded = responded;
        }
    }

}
