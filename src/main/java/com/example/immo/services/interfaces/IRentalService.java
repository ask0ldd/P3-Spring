package com.example.immo.services.interfaces;

import com.example.immo.dto.responses.ResponseRentalDto;
import com.example.immo.models.Rental;

public interface IRentalService {
    Rental getRental(final Long id);

    ResponseRentalDto getReturnableRental(final Long id);

    Iterable<Rental> getRentals();

    Iterable<ResponseRentalDto> getReturnableRentals();

    Rental saveRental(Rental rental);

    void deleteRental(final Long id);
}
