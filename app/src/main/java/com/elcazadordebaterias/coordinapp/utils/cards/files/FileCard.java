package com.elcazadordebaterias.coordinapp.utils.cards.files;

public class FileCard {

    private String fileName;
    private String uploaderName;
    private String uploadedDate;
    private String downloadLink;

    public FileCard(String fileName, String uploaderName, String uploadedDate, String downloadLink) {
        this.fileName = fileName;
        this.uploaderName = uploaderName;
        this.uploadedDate = uploadedDate;
        this.downloadLink = downloadLink;
    }


    public String getFileName() {
        return fileName;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public String getUploadedDate() {
        return uploadedDate;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

}
