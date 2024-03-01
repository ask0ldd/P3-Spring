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

import com.example.immo.dto.ReturnableLoggedUserDto;
import com.example.immo.dto.LoginDto;
import com.example.immo.dto.RegistrationDto;
import com.example.immo.dto.TokenResponseDto;
import com.example.immo.models.User;
import com.example.immo.services.AuthService;
import com.example.immo.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/auth")
// @CrossOrigin(origins = "http://localhost:5173")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> userRegister(@RequestBody RegistrationDto body) {
        authService.registerUser(body.getEmail(), body.getUsername(), body.getPassword());
        TokenResponseDto token = authService.loginUser(body.getEmail(), body.getPassword());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> userLogin(@RequestBody LoginDto body) {
        TokenResponseDto token = authService.loginUser(body.getEmail(), body.getPassword());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String email = principal.getName();
        User loggedUser = userService.getUserByEmail(email);
        return new ResponseEntity<ReturnableLoggedUserDto>(new ReturnableLoggedUserDto(loggedUser),
                HttpStatus.OK);
    }
}