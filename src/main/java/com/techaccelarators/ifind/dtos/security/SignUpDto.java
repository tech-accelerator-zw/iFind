package com.techaccelarators.ifind.dtos.security;

import lombok.Data;

@Data
public class SignUpDto {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}
