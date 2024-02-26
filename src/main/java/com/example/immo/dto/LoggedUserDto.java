package com.example.immo.dto;

import com.example.immo.models.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoggedUserDto { // return a simplified version of the user model
    private Long userId;
    /*
     * private String firstname;
     * private String lastname;
     */
    private String name;
    private String email;

    public LoggedUserDto(User user) {
        this.userId = user.getUserId();
        /*
         * this.firstname = user.getFirstname();
         * this.lastname = user.getLastname();
         */
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
