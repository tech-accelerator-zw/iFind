package com.techaccelarators.ifind.dtos.security;

import lombok.Data;

@Data
public class LoginDto {
    private String usernameOrEmail;
    private String password;
}
