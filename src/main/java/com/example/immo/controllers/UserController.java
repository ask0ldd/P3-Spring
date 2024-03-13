package com.example.immo.controllers;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.immo.dto.responses.ResponseUserDto;
import com.example.immo.services.UserService;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Retrieve the target User
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") final Long id) {
        try {
            if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access");
            }
            ResponseUserDto user = new ResponseUserDto(userService.getUser(id));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(user, headers, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find the requested User.", HttpStatus.NOT_FOUND);
        }
    }
}
