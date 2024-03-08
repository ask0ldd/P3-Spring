package com.example.immo.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.immo.dto.LoginDto;
import com.example.immo.dto.RegistrationDto;
import com.example.immo.dto.TokenResponseDto;
import com.example.immo.dto.responses.ResponseLoggedUserDto;
import com.example.immo.models.User;
import com.example.immo.services.AuthService;
import com.example.immo.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> userRegister(@RequestBody RegistrationDto body) {
        try {
            authService.registerUser(body.getEmail(), body.getUsername(), body.getPassword());
            TokenResponseDto token = authService.loginUser(body.getEmail(), body.getPassword());
            if (token == null || token.toString().isEmpty())
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginDto body) {
        try {
            TokenResponseDto token = authService.loginUser(body.getEmail(), body.getPassword());
            if (token == null) {
                return new ResponseEntity<String>("error", HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<TokenResponseDto>(token, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Retrieve the infos of the current authenticated User
    @GetMapping("/me")
    public ResponseEntity<?> getLoggedUser(HttpServletRequest request) {
        try {
            Principal principal = request.getUserPrincipal();
            if (principal == null) {
                return new ResponseEntity<String>("User not authenticated", HttpStatus.UNAUTHORIZED);
            }
            String email = principal.getName();
            User loggedUser = userService.getUserByEmail(email);
            if (loggedUser == null) {
                return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<ResponseLoggedUserDto>(new ResponseLoggedUserDto(loggedUser), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

/*
 * @GetMapping("/me")
 * public ResponseEntity<?> getLoggedUser(HttpServletRequest request) {
 * Principal principal = request.getUserPrincipal();
 * String email = principal.getName();
 * User loggedUser = userService.getUserByEmail(email);
 * return new ResponseEntity<ReturnableLoggedUserDto>(new
 * ReturnableLoggedUserDto(loggedUser),
 * HttpStatus.OK);
 * }
 */