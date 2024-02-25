package com.example.immo.dto;

import com.example.immo.models.Rental;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ReturnableRentalDto {
    private Long rentalId;
    private String description;
    private String name;
    private ReturnableUserDto owner;
    private String picture;
    private Float surface;
    private Float price;

    public ReturnableRentalDto(Rental rental) {
        this.rentalId = rental.getRentalId();
        this.description = rental.getDescription();
        this.name = rental.getName();
        this.picture = rental.getPicture();
        this.surface = rental.getSurface();
        this.price = rental.getPrice();
        this.owner = new ReturnableUserDto(rental.getOwner());
    }
}