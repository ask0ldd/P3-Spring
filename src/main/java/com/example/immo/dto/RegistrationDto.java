package com.example.immo.dto;

public class RegistrationDto {
    private String username;
    private String password;

    public RegistrationDto(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "Registration infos = " + this.username + " : " + this.password;
    }

}