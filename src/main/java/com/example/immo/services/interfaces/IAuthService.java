package com.example.immo.services.interfaces;

import com.example.immo.dto.TokenResponseDto;
import com.example.immo.models.User;

public interface IAuthService {

    TokenResponseDto registerUser(String email, String username, String password);

    TokenResponseDto loginUser(String email, String password);

    User getUserInfos();
}
