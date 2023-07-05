package com.techaccelarators.ifind.dtos.security;

import lombok.Data;

@Data
public class JwtAuthResponse {
    private String accessToken;
    private final String tokenType = "Bearer";
    public JwtAuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
