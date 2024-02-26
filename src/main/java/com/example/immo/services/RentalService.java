package com.example.immo.services;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.immo.dto.ReturnableRentalDto;
import com.example.immo.exceptions.UserNotFoundException;
import com.example.immo.models.Rental;
import com.example.immo.repositories.RentalRepository;

import lombok.Data;

@Data
@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    public Rental getRental(final Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target rental can't be found."));
        return rental;
    }

    public ReturnableRentalDto getReturnableRental(final Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target rental can't be found."));
        return new ReturnableRentalDto(rental);
    }

    public Iterable<Rental> getRentals() {
        Iterable<Rental> rentals = rentalRepository.findAll();
        if (!rentals.iterator().hasNext())
            throw new UserNotFoundException("Can't find any Rental.");
        return rentals;
    }

    public Iterable<ReturnableRentalDto> getReturnableRentals() {
        Iterable<Rental> rentals = rentalRepository.findAll();
        if (!rentals.iterator().hasNext())
            throw new UserNotFoundException("Can't find any Rental.");
        Iterable<ReturnableRentalDto> returnableRentals = StreamSupport.stream(rentals.spliterator(), false)
                .map(rental -> {
                    ReturnableRentalDto returnableRental = new ReturnableRentalDto(rental);
                    return returnableRental;
                })
                .collect(Collectors.toList());
        return returnableRentals;
    }

    public Rental saveRental(Rental rental) {
        Rental savedRental = rentalRepository.save(rental);
        return savedRental;
    }

    public void deleteRental(final Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target rental can't be found."));
        rentalRepository.delete(rental);
    }
}
