package com.example.project.Object;

public class User {
    private String emai, password, role;

    public String getEmai() {
        return emai;
    }

    public void setEmai(String emai) {
        this.emai = emai;
    }

    public String getPassword() {
        return password;
    }

    public User(String emai, String role) {
        this.emai = emai;
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User() {
    }

    public User(String emai, String password, String role) {
        this.emai = emai;
        this.password = password;
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "emai='" + emai + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
