package com.elcazadordebaterias.coordinapp.utils.cards;

import java.util.ArrayList;
import java.util.HashMap;

public class TestCard {

    private HashMap<String, ArrayList<String>> hashMap;

    public TestCard(HashMap<String, ArrayList<String>> hashMap) {
        this.hashMap = hashMap;
    }

    public HashMap<String, ArrayList<String>> getHashMap() {
        return hashMap;
    }
}
