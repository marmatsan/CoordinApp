package com.elcazadordebaterias.coordinapp.utils.cards.interactivity.studentcards;

import com.elcazadordebaterias.coordinapp.utils.customdatamodels.InteractivityCardType;

public class StandByCard extends InteractivityCard {

    private String spokerName;
    private int cardType;

    public StandByCard(String cardTitle, String studentID, String spokerName, int cardType) {
        super(cardTitle, studentID);
        this.spokerName = spokerName;
        this.cardType = cardType;
    }

    public String getSpokerName() {
        return spokerName;
    }

    public String getCardType() {
        String cardType;
        if (this.cardType == InteractivityCardType.TYPE_INPUTTEXT) {
            cardType = "Actividad de tipo entrada de texto";
        } else if (this.cardType == InteractivityCardType.TYPE_CHOICES) {
            cardType = "Actividad de tipo cuestionario";
        } else {
            cardType = "Recordatorio";
        }
        return cardType;
    }
}
