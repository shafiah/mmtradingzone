package com.example.mmtradingzone.network;

public class UserResponse {

    private Long id;
    private String userName;
    private String emailId;
    private String phoneNumber;
    private String deviceId;
    private boolean prime;

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}