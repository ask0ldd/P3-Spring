package com.example.immo.controllers;

import java.security.Principal;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.immo.dto.responses.ResponseDefaultDto;
import com.example.immo.dto.responses.ResponseRentalDto;
import com.example.immo.dto.responses.ResponseRentalsDto;
import com.example.immo.models.Rental;
import com.example.immo.models.User;
import com.example.immo.services.FileService;
import com.example.immo.services.RentalService;
import com.example.immo.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:4200")
public class RentalController {

    private final RentalService rentalService;
    private final UserService userService;
    private final FileService fileService;

    public RentalController(RentalService rentalService, UserService userService, FileService fileService) {
        this.rentalService = rentalService;
        this.userService = userService;
        this.fileService = fileService;
    }

    // Retrieve all Rentals
    @GetMapping("/rentals")
    public ResponseEntity<?> getRentals() {
        try {
            if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access");
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Iterable<ResponseRentalDto> rentals = rentalService.getReturnableRentals();
            return new ResponseEntity<>(new ResponseRentalsDto(rentals), headers, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find any Rental.", HttpStatus.NOT_FOUND);
        }
    }

    // Retrieve the target Rental
    @GetMapping("/rentals/{id}")
    public ResponseEntity<?> getRental(@PathVariable("id") final Long id) {
        try {
            if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access");
            }
            ResponseRentalDto rental = rentalService.getReturnableRental(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(rental, headers, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find the requested Rental.", HttpStatus.NOT_FOUND);
        }
    }

    // Update the target Rental
    @PutMapping("/rentals/{id}")
    public ResponseEntity<?> updateRental(@PathVariable("id") final Long id,
            @RequestParam("name") String name,
            @RequestParam("surface") Integer surface,
            @RequestParam("price") Integer price,
            @RequestParam("description") String description) {
        try {
            if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access");
            }
            Rental rental = rentalService.getRental(id);

            if (rental == null) {
                return new ResponseEntity<String>("Can't find the requested Rental.", HttpStatus.NOT_FOUND);
            }

            // !!!!!!!!!!! should validate datas
            rental.setName(name);
            rental.setSurface(surface);
            rental.setPrice(price);
            rental.setDescription(description);

            Rental modifiedRental = rentalService.saveRental(rental);
            System.out.println(modifiedRental);

            return new ResponseEntity<ResponseDefaultDto>(new ResponseDefaultDto("Rental updated !"),
                    HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find the requested Rental.", HttpStatus.NOT_FOUND);
        }
    }

    // Create a new Rental
    @PostMapping("/rentals")
    public ResponseEntity<?> createRental(HttpServletRequest request, @RequestParam("name") String name,
            @RequestParam("surface") String surface,
            @RequestParam("price") String price, @RequestParam("picture") MultipartFile picture,
            @RequestParam("description") String description) {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access");
        }

        if (picture.isEmpty()) {
            return new ResponseEntity<String>("A picture is needed!", HttpStatus.BAD_REQUEST);
        }

        // !!!!!!!!!!! should validate datas

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

        return new ResponseEntity<ResponseDefaultDto>(new ResponseDefaultDto("Rental created !"), HttpStatus.OK);
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

// HttpHeaders headers = new HttpHeaders();
// headers.setContentType(MediaType.APPLICATION_JSON);