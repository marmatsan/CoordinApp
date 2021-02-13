package com.elcazadordebaterias.coordinapp.utils;

import java.util.List;

public class ParentParentItem {

    private String ParentParentItemTitle;
    private List<ParentItem> ParentItemList;

    public ParentParentItem(String ParentParentItemTitle, List<ParentItem> ParentItemList){
        this.ParentParentItemTitle = ParentParentItemTitle;
        this.ParentItemList = ParentItemList;
    }

    public String getParentParentItemTitle() {
        return ParentParentItemTitle;
    }

    public void setParentParentItemTitle(String parentParentItemTitle) {
        ParentParentItemTitle = parentParentItemTitle;
    }

    public List<ParentItem> getParentItemList() {
        return ParentItemList;
    }

    public void setParentItemList(List<ParentItem> parentItemList) {
        ParentItemList = parentItemList;
    }

}
