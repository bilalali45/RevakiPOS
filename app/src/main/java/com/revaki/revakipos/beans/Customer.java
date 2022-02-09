package com.revaki.revakipos.beans;

public class Customer {
    private String Id;
    private String FullName;
    private String ContactNo;
    private String Address;
    private String RewardBalance;

    public Customer() {

    }

    public Customer(String id, String fullName, String contactNo, String address, String rewardBalance) {
        Id = id;
        FullName = fullName;
        ContactNo = contactNo;
        Address = address;
        RewardBalance = rewardBalance;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getRewardBalance() {
        return RewardBalance;
    }

    public void setRewardBalance(String rewardBalance) {
        RewardBalance = rewardBalance;
    }
}
