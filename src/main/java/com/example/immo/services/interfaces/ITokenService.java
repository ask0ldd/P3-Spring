package com.example.immo.services.interfaces;

import org.springframework.security.core.Authentication;

public interface ITokenService {
    String generateJwt(Authentication auth);
}
