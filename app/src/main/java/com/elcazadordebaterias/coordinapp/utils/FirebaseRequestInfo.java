package com.elcazadordebaterias.coordinapp.utils;

/**
 * Class that represents the information of a document in the Firebase collection named Requests.
 * It is used in the AdministrationFragment_Teacher_Courses class to get the information of pending petitions
 * and populate the cards that represent the petitions in CardItemRequest class.
 *
 * @author Martín Mateos Sánchez
 */
public class FirebaseRequestInfo {

    private String CourseNumber;
    private String CourseNumberLetter;
    private String StudentName;
    private String TeacherId;

    public FirebaseRequestInfo(){

    }

    public String getCourseNumber() {
        return CourseNumber;
    }

    public String getCourseNumberLetter() {
        return CourseNumberLetter;
    }

    public String getStudentName() {
        return StudentName;
    }

    public String getTeacherId() {
        return TeacherId;
    }

}
