package com.example.immo.dto.responses;

import java.util.Date;

import com.example.immo.models.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseLoggedUserDto { // return a simplified version of the user model
    private Long id;
    private String name;
    private String email;
    private Date created_at;
    private Date updated_at;

    public ResponseLoggedUserDto(User user) {
        this.id = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.created_at = user.getCreation();
        this.updated_at = user.getUpdate();
    }
}
