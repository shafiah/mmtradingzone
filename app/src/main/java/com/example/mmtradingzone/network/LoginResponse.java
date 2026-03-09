package com.example.mmtradingzone.network;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    private Long id;
    @SerializedName("userName")
    private String userName;
    private String phoneNumber;
    private String deviceId;
    private boolean prime;

    private Long loginVersion;

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isPrime() {
        return prime;
    }

    public Long getLoginVersion() {
        return loginVersion;
    }

    public void setLoginVersion(Long loginVersion) {
        this.loginVersion = loginVersion;
    }
}