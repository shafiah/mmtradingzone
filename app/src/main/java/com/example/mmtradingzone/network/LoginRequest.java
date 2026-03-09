package com.example.mmtradingzone.network;

public class LoginRequest {

    private String phoneNumber;
    private String password;
    private String deviceId;

    public LoginRequest(String phoneNumber, String password, String deviceId) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.deviceId = deviceId;
    }
}