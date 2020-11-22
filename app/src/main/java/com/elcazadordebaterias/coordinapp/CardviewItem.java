package com.elcazadordebaterias.coordinapp;

import android.widget.ExpandableListView;

public class CardviewItem {

    private ExpandableListView mItemCardviewExpandableListView;

    public CardviewItem(ExpandableListView itemCardviewExpandableListView){
        mItemCardviewExpandableListView = itemCardviewExpandableListView;
    }

    public ExpandableListView getExpandableListView(){
        return mItemCardviewExpandableListView;
    }

}
