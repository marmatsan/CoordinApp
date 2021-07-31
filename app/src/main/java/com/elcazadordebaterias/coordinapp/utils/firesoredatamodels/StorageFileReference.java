package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;

public class StorageFileReference {

    private String fileName;
    private String downloadLink;

    public StorageFileReference(){

    }

    public StorageFileReference(String fileName, String downloadLink) {
        this.fileName = fileName;
        this.downloadLink = downloadLink;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

}
