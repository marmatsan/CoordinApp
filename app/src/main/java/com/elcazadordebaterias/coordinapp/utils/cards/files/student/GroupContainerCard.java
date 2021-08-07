package com.elcazadordebaterias.coordinapp.utils.cards.files.student;

import com.elcazadordebaterias.coordinapp.utils.cards.files.FilesContainerCard;

import java.util.ArrayList;

public class GroupContainerCard {

    private String name;
    private ArrayList<FilesContainerCard> filesContainerList;

    public GroupContainerCard(String name, ArrayList<FilesContainerCard> filesContainerList) {
        this.name = name;
        this.filesContainerList = filesContainerList;
    }

    public String getName() {
        return name;
    }

    public ArrayList<FilesContainerCard> getFilesContainerList() {
        return filesContainerList;
    }

}
