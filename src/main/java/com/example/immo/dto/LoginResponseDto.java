package com.example.immo.dto;

import com.example.immo.models.User;

public class LoginResponseDto {
    private LoggedUserDto user;
    private String jwt;

    public LoginResponseDto() {
        super();
    }

    public LoginResponseDto(User user, String jwt) {
        super();
        this.user = new LoggedUserDto(user); // return a simplified version of the user model
        this.jwt = jwt;
    }

    public LoggedUserDto getUser() {
        return this.user;
    }

    public String getJwt() {
        return this.jwt;
    }

    public void setUser(LoggedUserDto user) {
        this.user = user;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

}