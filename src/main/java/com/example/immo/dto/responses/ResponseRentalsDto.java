package com.example.immo.dto.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ResponseRentalsDto {
    private Iterable<ResponseRentalDto> rentals;

    public ResponseRentalsDto(Iterable<ResponseRentalDto> rentals) {
        this.rentals = rentals;
    }
}
