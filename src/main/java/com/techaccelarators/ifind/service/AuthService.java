package com.techaccelarators.ifind.service;


import com.techaccelarators.ifind.dtos.VerifyTokenRequestDTO;
import com.techaccelarators.ifind.dtos.security.LoginDto;
import com.techaccelarators.ifind.dtos.security.SignUpDto;

public interface AuthService {
    String authenticateUser(LoginDto loginDto);
    void registerUser( SignUpDto signUpDto);

    String findEmailByUsername(String key);
}
