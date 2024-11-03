package com.example.project.Object;

public class Address {
    private String userName, address, phoneNumber;

    public Address() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address(String userName, String address, String phoneNumber) {
        this.userName = userName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
