package com.techaccelarators.ifind.controller;

import com.techaccelarators.ifind.dtos.VerifyTokenRequestDTO;
import com.techaccelarators.ifind.dtos.security.JwtAuthResponse;
import com.techaccelarators.ifind.dtos.security.LoginDto;
import com.techaccelarators.ifind.dtos.security.SignUpDto;
import com.techaccelarators.ifind.security.JwtTokenProvider;
import com.techaccelarators.ifind.service.AuthService;
import com.techaccelarators.ifind.service.impl.OtpService;
import com.techaccelarators.ifind.util.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    private final JwtTokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    private final OtpService otpService;


    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword());
        try
        {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            String token = tokenProvider.createToken(authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return new ResponseEntity<>(new JwtAuthResponse(token), HttpStatus.OK);
        }
        catch (AuthenticationException exception) {
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
        String username = verifyTokenRequest.getUsername();
        Integer otp = verifyTokenRequest.getOtp();

        boolean isOtpValid = otpService.validateOTP(username, otp);
        if (!isOtpValid) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = tokenProvider.createTokenAfterVerifiedOtp(username);
        JwtAuthResponse response = new JwtAuthResponse(token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
