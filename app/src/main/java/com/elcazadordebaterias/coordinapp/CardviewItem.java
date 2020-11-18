package com.elcazadordebaterias.coordinapp;

import android.widget.ExpandableListView;

public class CardviewItem {

    private int mItemCardviewImage;
    private ExpandableListView mItemCardviewExpandableListView;

    public CardviewItem(int itemCardviewImage, ExpandableListView itemCardviewExpandableListView){
        mItemCardviewImage = itemCardviewImage;
        mItemCardviewExpandableListView = itemCardviewExpandableListView;
    }

    public int getItemCardviewImage(){
        return mItemCardviewImage;
    }

    public ExpandableListView getExpandableListView(){
        return mItemCardviewExpandableListView;
    }

}
