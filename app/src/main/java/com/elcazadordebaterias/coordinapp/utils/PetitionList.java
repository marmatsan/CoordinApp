package com.elcazadordebaterias.coordinapp.utils;

import java.util.ArrayList;

public class PetitionList {

    private ArrayList<PetitionRequest> petitionList;

    public PetitionList(){

    }

    public ArrayList<PetitionRequest> getPetitionList() {
        return petitionList;
    }

    public void setPetitionsList(ArrayList<PetitionRequest> petitionsList) {
        petitionList = petitionsList;
    }
}
