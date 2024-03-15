package com.example.immo.services;

import java.util.HashSet;
import java.util.Set;

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
import com.example.immo.services.interfaces.IAuthService;

@Service
@Transactional
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public TokenResponseDto registerUser(String email, String username, String password) {
        try {
            String encodedPassword = passwordEncoder.encode(password);

            Role userRole = roleRepository.findByAuthority("USER")
                    .orElseThrow(() -> new RuntimeException("User role not found")); // .get()
            Set<Role> authorities = new HashSet<>();
            authorities.add(userRole);

            userRepository.save(new User(null, username, email,
                    encodedPassword, authorities));

            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));
            String token = tokenService.generateJwt(auth);
            return new TokenResponseDto(token);
        } catch (AuthenticationException e) {
            return new TokenResponseDto(""); // maybe 40x error instead
        }

    }

    public TokenResponseDto loginUser(String email, String password) {
        try {
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));
            String token = tokenService.generateJwt(auth);
            return new TokenResponseDto(token);
        } catch (AuthenticationException e) {
            return new TokenResponseDto("");
        }
    }

    public User getUserInfos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return user;
    }
}