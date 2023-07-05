package com.techaccelarators.ifind.service;


import com.techaccelarators.ifind.domain.UserAccount;
import com.techaccelarators.ifind.dtos.VerifyTokenRequestDTO;
import com.techaccelarators.ifind.dtos.security.JwtAuthResponse;
import com.techaccelarators.ifind.dtos.security.LoginDto;
import com.techaccelarators.ifind.dtos.security.SignUpDto;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    JwtAuthResponse authenticateUser(LoginDto loginDto);
    void registerUser( SignUpDto signUpDto);

    void forgotPassword(String email, HttpServletRequest request);

    JwtAuthResponse verifyOtp(VerifyTokenRequestDTO verifyTokenRequest);

    void resetPassword(String token, String newPassword);
}
