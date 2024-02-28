package com.example.immo.dto;

public class RegistrationDto {
    private String username;
    private String email;
    private String password;

    public RegistrationDto(String email, String name, String password) {
        super();
        this.username = name;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "Registration infos = " + this.username + " : " + this.password;
    }

}