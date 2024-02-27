package com.example.immo.dto;

public class LoginDto {
    private String email;
    private String password;

    public LoginDto(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
