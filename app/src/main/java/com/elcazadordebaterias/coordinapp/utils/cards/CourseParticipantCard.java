package com.elcazadordebaterias.coordinapp.utils.cards;

import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.CourseParticipantAdapter;

/**
 * Object to hold the information of the layout card that represents an user (its name and its e-mail).
 * Used for both teacher and student.
 *
 * The layout of this object is {@link com.elcazadordebaterias.coordinapp.R.layout#utils_cards_courseparticipant}
 *
 * @see CourseParticipantAdapter
 * @author Martín Mateos Sánchez
 */

public class CourseParticipantCard {

    private int image;
    private String participantID;
    private String participantRole;
    private String mParticipantName;
    private String mParticipantEmail;

    public CourseParticipantCard(int image, String participantID, String participantRole, String participantName, String participantEmail) {
        this.image = image;
        this.participantID = participantID;
        this.participantRole = participantRole;
        this.mParticipantName = participantName;
        this.mParticipantEmail = participantEmail;
    }

    public int getImage() {
        return image;
    }

    public String getParticipantID() {
        return participantID;
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

