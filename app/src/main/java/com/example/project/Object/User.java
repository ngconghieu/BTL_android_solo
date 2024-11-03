package com.example.project.Object;

public class User {
    private String email, password, role;

    public String getEmai() {
        return email;
    }

    public void setEmai(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public User(String email, String role) {
        this.email = email;
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

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
