package com.techaccelarators.ifind.service;


import com.techaccelarators.ifind.domain.UserAccount;
import com.techaccelarators.ifind.dtos.security.LoginDto;
import com.techaccelarators.ifind.dtos.security.SignUpDto;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    String authenticateUser(LoginDto loginDto);
    void registerUser( SignUpDto signUpDto);

    String findEmailByUsername(String key);

    UserAccount getUserAccountByPasswordResetToken(String passwordResetToken);

    void forgotPassword(String email, HttpServletRequest request);

    void resetPassword(String token, String newPassword);
}
