package com.example.rideshare;

/**
 * A user with an account
 *
 * @author Sukriti
 * @version 1.0
 */
public class User {
    private String userId;
    private String phoneNumber;

    public User() {

    }

    public User(String userId, String phoneNumber) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

