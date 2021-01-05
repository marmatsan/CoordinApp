package com.elcazadordebaterias.coordinapp.utils;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

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
