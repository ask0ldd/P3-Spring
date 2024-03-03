package com.example.immo.services;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.immo.dto.responses.ResponseRentalDto;
import com.example.immo.exceptions.UserNotFoundException;
import com.example.immo.models.Rental;
import com.example.immo.repositories.RentalRepository;
import com.example.immo.services.interfaces.IRentalService;

import lombok.Data;

@Data
@Service
public class RentalService implements IRentalService {

    @Autowired
    private RentalRepository rentalRepository;

    public Rental getRental(final Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target rental cannot be found."));
        return rental;
    }

    public ResponseRentalDto getReturnableRental(final Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target rental cannot be found."));
        return new ResponseRentalDto(rental);
    }

    public Iterable<Rental> getRentals() {
        Iterable<Rental> rentals = rentalRepository.findAll();
        if (!rentals.iterator().hasNext())
            throw new UserNotFoundException("Can't find any Rental.");
        return rentals;
    }

    public Iterable<ResponseRentalDto> getReturnableRentals() {
        Iterable<Rental> rentals = rentalRepository.findAll();
        if (!rentals.iterator().hasNext())
            throw new UserNotFoundException("Can't find any Rental.");
        Iterable<ResponseRentalDto> returnableRentals = StreamSupport.stream(rentals.spliterator(), false)
                .map(rental -> {
                    ResponseRentalDto returnableRental = new ResponseRentalDto(rental);
                    return returnableRental;
                })
                .collect(Collectors.toList());
        return returnableRentals;
    }

    public Rental saveRental(Rental rental) {
        return Optional.of(rentalRepository.save(rental))
                .orElseThrow(() -> new RuntimeException("Failed to save the rental."));
        /*
         * try {
         * Rental savedRental = rentalRepository.save(rental);
         * return savedRental;
         * } catch (Exception e) {
         * throw new RuntimeException("Failed to save message: " + e.getMessage());
         * }
         */
    }

    public void deleteRental(final Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target rental cannot be found."));
        rentalRepository.delete(rental);
    }
}
