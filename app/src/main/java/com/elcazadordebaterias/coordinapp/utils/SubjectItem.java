package com.elcazadordebaterias.coordinapp.utils;

/**
 * Class that represent a subject. Its properties are the icon of the subject (int) and
 * the name of the subject (string). It is used by the ListPopupWindowAdapter, which recieves
 * an arraylist of SubjectItem objects to be displayed.
 *
 * @author Martín Mateos Sánchez
 */
public class SubjectItem {

    private int subjectIcon;
    private String subjectName;

    public SubjectItem(int subjectIcon, String subjectName){
        this.subjectIcon = subjectIcon;
        this.subjectName = subjectName;

    }

    public int getSubjectIcon() {
        return subjectIcon;
    }

    public void setSubjectIcon(int subjectIcon) {
        this.subjectIcon = subjectIcon;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

}
