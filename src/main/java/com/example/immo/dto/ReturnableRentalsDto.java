package com.example.immo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ReturnableRentalsDto {
    private Iterable<ReturnableRentalDto> rentals;

    public ReturnableRentalsDto(Iterable<ReturnableRentalDto> rentals) {
        this.rentals = rentals;
    }
}
