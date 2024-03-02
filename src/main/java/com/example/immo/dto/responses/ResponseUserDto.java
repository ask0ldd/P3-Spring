package com.example.immo.dto.responses;

import java.util.Date;

import com.example.immo.models.User;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ResponseUserDto {

    private Long id;
    /*
     * private String firstname;
     * private String lastname;
     */
    private String name;
    private String email;
    private Date created_at;
    private Date updated_at;

    public ResponseUserDto(User user) {
        super();
        this.id = user.getUserId();
        /*
         * this.firstname = user.getFirstname();
         * this.lastname = user.getLastname();
         */
        this.name = user.getName();
        this.email = user.getEmail();
        this.created_at = user.getCreation();
        this.updated_at = user.getUpdate();
    }
}
