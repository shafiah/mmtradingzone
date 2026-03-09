package com.example.mmtradingzone.network;

public class UserRequest {

    private String userName;
    private String emailId;
    private String phoneNumber;
    private String deviceId;
    private String password;

    public UserRequest(String userName, String emailId, String phoneNumber, String deviceId, String password) {
        this.userName = userName;
        this.emailId = emailId;
        this.phoneNumber = phoneNumber;
        this.deviceId = deviceId;
        this.password = password;
    }
}