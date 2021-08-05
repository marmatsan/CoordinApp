package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;

public class StorageFile {

    private String uploaderName;
    private String fileName;
    private String uploadedDate;
    private String downloadLink;

    public StorageFile(){

    }

    public StorageFile(String uploaderName, String fileName, String uploadedDate, String downloadLink) {
        this.uploaderName = uploaderName;
        this.fileName = fileName;
        this.uploadedDate = uploadedDate;
        this.downloadLink = downloadLink;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUploadedDate() {
        return uploadedDate;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

}
