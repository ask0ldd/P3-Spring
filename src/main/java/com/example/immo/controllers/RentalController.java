package com.example.immo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.immo.models.Rental;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @GetMapping("/rentals")
    public ResponseEntity<?> getUsers() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Iterable<ReturnableRentalDto> rentals = rentalService.getReturnableRentals();
            return new ResponseEntity<>(rentals, headers, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find any Rental.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/rental/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") final Long id) {
        try {
            ReturnableRentalDto rental = rentalService.getReturnableRental(id);
            return new ResponseEntity<>(rental, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find the requested Rental.", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/rental/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") final Long id, @RequestBody Rental rental) {
        try {
            Rental currentRental = rentalService.getRental(id);

            String name = rental.getName();
            if (name != null) { // needs validation
                currentRental.setName(name);
            }

            String desc = rental.getDescription();
            if (desc != null) { // needs validation
                currentRental.setDescription(desc);
            }

            Rental modifiedRental = rentalService.saveRental(currentRental);

            System.out.println(modifiedRental);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(new ReturnableRentalDto(modifiedRental), headers,
                    HttpStatus.OK);
        } catch (Exception exception) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<String>("Can't find the requested Rental.", headers, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/rental/{id}")
    public ResponseEntity<?> deleteRental(@PathVariable("id") final Long id) {
        try {
            rentalService.deleteRental(id);
            return new ResponseEntity<String>("Message deleted.", HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find the requested Message.", HttpStatus.NOT_FOUND);
        }
    }

}
