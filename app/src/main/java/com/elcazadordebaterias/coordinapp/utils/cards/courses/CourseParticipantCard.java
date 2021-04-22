package com.elcazadordebaterias.coordinapp.utils.cards.courses;

import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.courses.CourseParticipantAdapter;

/**
 * Object to hold the information of the layout card that represents an user (its name and its e-mail).
 * Used for both teacher and student.
 *
 * The layout of this object is {@link com.elcazadordebaterias.coordinapp.R.layout#utils_courseparticipant}
 *
 * @see CourseParticipantAdapter
 * @author Martín Mateos Sánchez
 */

public class CourseParticipantCard {

    private String participantRole;
    private String mParticipantName;
    private String mParticipantEmail;

    public CourseParticipantCard(String participantRole, String participantName, String participantEmail) {
        this.participantRole = participantRole;
        this.mParticipantName = participantName;
        this.mParticipantEmail = participantEmail;
    }

    public String getParticipantRole() {
        return participantRole;
    }

    public String getParticipantName() {
        return mParticipantName;
    }

    public String getParticipantEmail() {
        return mParticipantEmail;
    }

}

