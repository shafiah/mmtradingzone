package com.example.mmtradingzone.models;

public class ResponseModel {

    private String message;
    private String error;

    private boolean paid;


    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(String error) {
        this.error = error;
    }
}
