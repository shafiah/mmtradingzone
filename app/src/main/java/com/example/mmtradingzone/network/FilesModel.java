package com.example.mmtradingzone.network;

public class FilesModel {

    private Long id;
    private String fileName;
    private String fileType;
    private boolean paid;

    private String title;

    public String getFileName() {
        return fileName;
    }

    public boolean isPaid(){
        return paid;
    }

    public String getTitle(){
        return title;
    }

    public Long getId(){
        return id;
    }
    public  void setId(Long id){
        this.id = id;
    }



}
