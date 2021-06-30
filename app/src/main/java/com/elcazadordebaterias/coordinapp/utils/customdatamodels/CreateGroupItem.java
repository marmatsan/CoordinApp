package com.elcazadordebaterias.coordinapp.utils.customdatamodels;

/**
 Used in the listpopupwindow of the teacher when creategroupbutton is created
 */
public class CreateGroupItem {

    private int itemIcon;
    private String itemText;

    public CreateGroupItem(int itemIcon, String itemText){
        this.itemIcon = itemIcon;
        this.itemText = itemText;

    }

    public int getSubjectIcon() {
        return itemIcon;
    }

    public void setSubjectIcon(int subjectIcon) {
        this.itemIcon = subjectIcon;
    }

    public String getSubjectName() {
        return itemText;
    }

    public void setSubjectName(String subjectName) {
        this.itemText = subjectName;
    }

}
