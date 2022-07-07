package com.techaccelarators.ifind.controller;

import com.techaccelarators.ifind.dtos.security.JwtAuthResponse;
import com.techaccelarators.ifind.dtos.security.LoginDto;
import com.techaccelarators.ifind.dtos.security.SignUpDto;
import com.techaccelarators.ifind.service.AuthService;
import com.techaccelarators.ifind.util.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;


    @PostMapping("/sign-in")
    public Response<JwtAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){

        String token = authService.authenticateUser(loginDto);
        return new Response<JwtAuthResponse>().buildSuccessResponse("SUCCESS",
                new JwtAuthResponse(token), HttpStatus.OK);
    }
    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){
        authService.registerUser(signUpDto);
        return new ResponseEntity<>("User Registered Successfully",HttpStatus.OK);

    }
}
