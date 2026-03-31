package com.example.mmtradingzone.network;

public class Users {

    private Long id;
    private String userName;
    private String phoneNumber;

    // getters setters
    public Long getId() { return id; }

    public String getUserName() { return userName; }

    public String getPhoneNumber() { return phoneNumber; }

    // 🔥 ADD THIS (VERY IMPORTANT)
    public void setId(Long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}