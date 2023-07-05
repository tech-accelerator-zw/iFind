package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.dtos.EmailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Description(value = "Service responsible for handling OTP related functionality.")
@Service
public class OtpService {

    private final Logger LOGGER = LoggerFactory.getLogger(OtpService.class);

    private OtpGenerator otpGenerator;
    private EmailService emailService;
    private UserService userService;
    @Value("${spring.mail.username}")
    private String username;
    public OtpService(OtpGenerator otpGenerator, EmailService emailService, UserService userService)
    {
        this.otpGenerator = otpGenerator;
        this.emailService = emailService;
        this.userService = userService;
    }
    public Boolean generateOtp(String key) {
        Integer otpValue = otpGenerator.generateOTP(key);
        if (otpValue == -1) {
            LOGGER.error("OTP generator is not working...");
            return  false;
        }
        LOGGER.info("Generated OTP: {}", otpValue);
        String userEmail = userService.findEmailByUsername(key);
        List<String> recipients = new ArrayList<>();
        recipients.add(userEmail);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setSubject("Here's your One Time Password (OTP) - Expire in 5 minutes!");
        emailDTO.setBody("OTP Password: " + otpValue);
        emailDTO.setRecipients(recipients);

        return emailService.sendSimpleMessage(emailDTO);
    }
    public Boolean validateOTP(String key, Integer otpNumber) {
        Integer cacheOTP = otpGenerator.getOPTByKey(key);
        if (cacheOTP!=null && cacheOTP.equals(otpNumber)) {
            otpGenerator.clearOTPFromCache(key);
            return true;
        }
        return false;
    }
}
