package com.elcazadordebaterias.coordinapp.utils.cards.files;

public class FileCard {

    private int fileRepresentativeImage;
    private String uploaderName;
    private String uploadedDate;

    public FileCard(int fileRepresentativeImage, String uploaderName, String uploadedDate) {
        this.fileRepresentativeImage = fileRepresentativeImage;
        this.uploaderName = uploaderName;
        this.uploadedDate = uploadedDate;
    }

    public int getFileRepresentativeImage() {
        return fileRepresentativeImage;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public String getUploadedDate() {
        return uploadedDate;
    }
}
