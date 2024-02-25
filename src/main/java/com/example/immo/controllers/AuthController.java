package com.example.immo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.immo.dto.LoginResponseDto;
import com.example.immo.dto.RegistrationDto;
import com.example.immo.models.User;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public User userRegister(@RequestBody RegistrationDto body) {
        return authService.registerUser(body.getUsername(), body.getPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> userLogin(@RequestBody RegistrationDto body) {
        System.out.println("********************* LOGIN ***************************");
        LoginResponseDto loggedUser = authService.loginUser(body.getUsername(), body.getPassword());
        return new ResponseEntity<>(loggedUser, HttpStatus.OK);
    }

}

/*
 * private static final Logger LOG =
 * LoggerFactory.getLogger(AuthController.class);
 * 
 * private TokenService tokenService;
 * 
 * public AuthController(TokenService tokenService) {
 * this.tokenService = tokenService;
 * }
 * 
 * @PostMapping("/token")
 * public String token(Authentication authentication) {
 * LOG.debug("Token requested for user : '{}'", authentication.getName());
 * String token = this.tokenService.generateToken(authentication);
 * LOG.debug("token granted : {}", token);
 * System.out.println(token);
 * return token;
 * }
 */