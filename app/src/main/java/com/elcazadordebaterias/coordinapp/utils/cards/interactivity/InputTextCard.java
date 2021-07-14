package com.elcazadordebaterias.coordinapp.utils.cards.interactivity;

public class InputTextCard extends InteractivityCard {

    private String inputText;

    public InputTextCard(String cardTitle) {
        super(cardTitle);
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

}
