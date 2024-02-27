package com.example.immo.dto;

public class TokenResponseDto {

    private String token;

    public TokenResponseDto(String token) {
        super();
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}