package com.elcazadordebaterias.coordinapp.utils.cards.files;

import java.util.ArrayList;

public class FilesContainerCard {

    private String groupName;
    private ArrayList<FileCard> filesList;

    public FilesContainerCard(String groupName, ArrayList<FileCard> filesList) {
        this.groupName = groupName;
        this.filesList = filesList;
    }

    public String getGroupName() {
        return groupName;
    }

    public ArrayList<FileCard> getFilesList() {
        return filesList;
    }

}
