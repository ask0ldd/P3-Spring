package com.example.immo.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.immo.dto.ReturnableRentalDto;
import com.example.immo.dto.ReturnableRentalsDto;
import com.example.immo.models.Rental;
import com.example.immo.models.User;
import com.example.immo.services.FileService;
import com.example.immo.services.RentalService;
import com.example.immo.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api")
// @CrossOrigin(origins = "http://localhost:5173")
@CrossOrigin(origins = "http://localhost:4200")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @GetMapping("/rentals")
    public ResponseEntity<?> getRentals() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Iterable<ReturnableRentalDto> rentals = rentalService.getReturnableRentals();
            return new ResponseEntity<>(new ReturnableRentalsDto(rentals), headers, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find any Rental.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/rentals/{id}")
    public ResponseEntity<?> getRental(@PathVariable("id") final Long id) {
        try {
            ReturnableRentalDto rental = rentalService.getReturnableRental(id);
            return new ResponseEntity<>(rental, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find the requested Rental.", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/rentals/{id}")
    public ResponseEntity<?> updateRental(@PathVariable("id") final Long id, @RequestBody Rental rental) {
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

    /*
     * @DeleteMapping("/rentals/{id}")
     * public ResponseEntity<?> deleteRental(@PathVariable("id") final Long id) {
     * try {
     * rentalService.deleteRental(id);
     * return new ResponseEntity<String>("Message deleted.", HttpStatus.OK);
     * } catch (Exception exception) {
     * return new ResponseEntity<String>("Can't find the requested Message.",
     * HttpStatus.NOT_FOUND);
     * }
     * }
     */

    @PostMapping("/rentals")
    public ResponseEntity<?> createRental(HttpServletRequest request, @RequestParam("name") String name,
            @RequestParam("surface") String surface,
            @RequestParam("price") String price, @RequestParam("picture") MultipartFile picture,
            @RequestParam("description") String description) {

        if (picture.isEmpty()) {
            return new ResponseEntity<String>("A picture is needed!", HttpStatus.BAD_REQUEST);
        }

        String filename = fileService.save(picture);

        Principal principal = request.getUserPrincipal();
        String email = principal.getName();
        User loggedUser = userService.getUserByEmail(email);

        // 127.0.0.1:3001/img/rental/griff.jpg
        Rental rental = Rental.builder().name(name).rentalId(null).owner(loggedUser)
                .description(description)
                // .picture(picture.getOriginalFilename())
                .picture("http://127.0.0.1:3001/img/rental/" + filename)
                .surface(Integer.parseInt(surface)).price(Integer.parseInt(price)).build();

        rentalService.saveRental(rental);

        return new ResponseEntity<String>("Rental created !", HttpStatus.OK);
    }

}
