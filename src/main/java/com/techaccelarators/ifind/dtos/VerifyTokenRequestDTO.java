package com.techaccelarators.ifind.dtos;


import javax.validation.constraints.NotNull;

public class VerifyTokenRequestDTO {

    @NotNull
    private String username;

    @NotNull
    private Integer otp;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }
}
