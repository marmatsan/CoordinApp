package com.elcazadordebaterias.coordinapp.utils;

/**
 * Object to hold the information of the layout that represents an user (its name and its e-mail).
 * Used for both teacher and student.
 *
 * The layout of this object is {@link com.elcazadordebaterias.coordinapp.R.layout#utils_courseparticipant}
 *
 * @see com.elcazadordebaterias.coordinapp.adapters.CourseParticipantAdapter
 * @author Martín Mateos Sánchez
 */

public class CourseParticipant {

    private String mParticipantName;
    private String mParticipantEmail;

    public CourseParticipant(String participantName, String participantEmail) {
        this.mParticipantName = participantName;
        this.mParticipantEmail = participantEmail;
    }

    public String getParticipantName() {
        return mParticipantName;
    }

    public String getParticipantEmail() {
        return mParticipantEmail;
    }

}

