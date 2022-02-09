package com.revaki.revakipos.beans;

import android.graphics.Bitmap;

public class User {
    private String userId;
    private String sessionUsername;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String mobileNo;
    private String email;
    private int posKey;
    private int modificationKey;


    public User() {

    }

    public User(String userId, String uniqueId, String firstName, String lastName, String mobileNo, String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNo = mobileNo;
        this.email = email;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPosKey() {
        return posKey;
    }

    public void setPosKey(int posKey) {
        this.posKey = posKey;
    }

    public int getModificationKey() {
        return modificationKey;
    }

    public void setModificationKey(int modificationKey) {
        this.modificationKey = modificationKey;
    }

    public String getSessionUsername() {
        return sessionUsername;
    }

    public void setSessionUsername(String sessionUsername) {
        this.sessionUsername = sessionUsername;
    }
}
