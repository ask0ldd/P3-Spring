package com.example.immo.dto;

import java.util.Date;

import com.example.immo.models.Rental;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ReturnableRentalDto {
    private Long id;
    private String description;
    private String name;
    // private ReturnableUserDto owner;
    private Long owner_id;
    private String picture;
    private Integer surface;
    private Integer price;
    private Date created_at;
    private Date updated_at;

    public ReturnableRentalDto(Rental rental) {
        this.id = rental.getRentalId();
        this.description = rental.getDescription();
        this.name = rental.getName();
        this.picture = rental.getPicture();
        this.surface = rental.getSurface();
        this.price = rental.getPrice();
        this.owner_id = rental.getOwner().getUserId();
        this.created_at = rental.getCreation();
        this.updated_at = rental.getUpdate();
    }
}