package com.example.immo.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.immo.dto.TokenResponseDto;
import com.example.immo.models.Role;
import com.example.immo.models.User;
import com.example.immo.repositories.RoleRepository;
import com.example.immo.repositories.UserRepository;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    public TokenResponseDto registerUser(String email, String username, String password) {

        String encodedPassword = passwordEncoder.encode(password);

        Role userRole = roleRepository.findByAuthority("USER").get();
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        userRepository.save(new User(null, username, email,
                encodedPassword, authorities));

        try {
            Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            String token = tokenService.generateJwt(auth);
            return new TokenResponseDto(token);
        } catch (AuthenticationException e) {
            return new TokenResponseDto(""); // maybe 40x error instead
        }

    }

    public TokenResponseDto loginUser(String email, String password) {
        try {

            // System.out.println("\n\n***************" +
            // userRepository.findByEmail(username).get().getAuthorities() +
            // "***************\n\n");

            Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            String token = tokenService.generateJwt(auth);

            // return new LoginResponseDto(userRepository.findByEmail(email).get(), token);
            return new TokenResponseDto(token);
        } catch (AuthenticationException e) {
            return new TokenResponseDto(""); // maybe 40x error instead
        }
    }

    public User getUserInfos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return user;
    }
}