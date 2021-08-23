package com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards;

public class ReminderCard extends InteractivityCard {

    private String reminderText;

    public ReminderCard() {

    }

    public ReminderCard(String cardTitle, String reminderText) {
        super(cardTitle);
        this.reminderText = reminderText;
    }

    public String getReminderText() {
        return reminderText;
    }

    public void setReminderText(String reminderText) {
        this.reminderText = reminderText;
    }
}
