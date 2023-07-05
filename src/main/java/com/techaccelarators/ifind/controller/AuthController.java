package com.techaccelarators.ifind.controller;

import com.techaccelarators.ifind.dtos.VerifyTokenRequestDTO;
import com.techaccelarators.ifind.dtos.security.JwtAuthResponse;
import com.techaccelarators.ifind.dtos.security.LoginDto;
import com.techaccelarators.ifind.dtos.security.SignUpDto;
import com.techaccelarators.ifind.exception.UnauthorizedException;
import com.techaccelarators.ifind.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
@Tag(name = "Authentication Controller", description = "Rest Controller for Authentication")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
        try {
            JwtAuthResponse jwtAuthResponse = authService.authenticateUser(loginDto);
            return new ResponseEntity<>(jwtAuthResponse,HttpStatus.OK);
        } catch (AuthenticationException exception) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){
        authService.registerUser(signUpDto);
        return new ResponseEntity<>("User Registered Successfully",HttpStatus.OK);

    }
    @PostMapping("/forgot-password")
    public  ResponseEntity<?> forgotPassword(@RequestParam String email, HttpServletRequest request){
        authService.forgotPassword(email,request);
        return new ResponseEntity<>("A password reset link has been sent to "+email,HttpStatus.OK);
    }

    @PostMapping("/reset")
    public  ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword){
        authService.resetPassword(token, newPassword);
        return new ResponseEntity<>("You have Successfully reset your password, Try Logging in with the new password",HttpStatus.OK);
    }

    @PostMapping(value = "verify")
    public ResponseEntity<JwtAuthResponse> verifyOtp(@Valid @RequestBody VerifyTokenRequestDTO verifyTokenRequest) {
        try {
            return new ResponseEntity<>(authService.verifyOtp(verifyTokenRequest), HttpStatus.OK);
        }catch (UnauthorizedException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
