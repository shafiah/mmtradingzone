package com.example.mmtradingzone.network;

public class FilesModel {

    private long id;
    private String fileName;
    private String fileType;
    private boolean paid;
    private String uploadDate;
    private String title;

    // Getter & Setter
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public String getUploadDate() { return uploadDate; }
    public void setUploadDate(String uploadDate) { this.uploadDate = uploadDate; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }


}
