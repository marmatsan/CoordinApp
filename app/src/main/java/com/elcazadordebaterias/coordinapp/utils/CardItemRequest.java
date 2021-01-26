package com.elcazadordebaterias.coordinapp.utils;

/**
 * Class that represents the information that populates a card petition for joining a group.
 * It is used by representing the information at utils_requestsubjectcard
 *
 * @author Martín Mateos Sánchez
 */
public class CardItemRequest {

    private String studentname;
    private String coursenumber;

    public CardItemRequest(String studentname, String coursenumber){
        this.studentname = studentname;
        this.coursenumber = coursenumber;
    }

    public void setStudentName(String studentname) {
        this.studentname = studentname;
    }

    public String getStudentName() {
        return this.studentname;
    }

    public void setCourseNumber(String coursenumber) {
        this.coursenumber = coursenumber;
    }

    public String getCourseNumber() {
        return this.coursenumber;
    }

}
