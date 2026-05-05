package com.example.mmtradingzone.network;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    private Long id;
    @SerializedName("userName")
    private String userName;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    private String deviceId;
    private boolean prime;
    private String userType;

    private Long loginVersion;

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber(){return phoneNumber;}

    public boolean isPrime() {
        return prime;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getLoginVersion() {
        return loginVersion;
    }

    public void setLoginVersion(Long loginVersion) {
        this.loginVersion = loginVersion;
    }
}