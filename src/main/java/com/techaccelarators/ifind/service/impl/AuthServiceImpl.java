package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.Role;
import com.techaccelarators.ifind.domain.UserAccount;
import com.techaccelarators.ifind.domain.UserRole;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.VerifyTokenRequestDTO;
import com.techaccelarators.ifind.dtos.security.JwtAuthResponse;
import com.techaccelarators.ifind.dtos.security.LoginDto;
import com.techaccelarators.ifind.dtos.security.SignUpDto;
import com.techaccelarators.ifind.exception.InternalServerErrorException;
import com.techaccelarators.ifind.exception.InvalidRequestException;
import com.techaccelarators.ifind.exception.RecordNotFoundException;
import com.techaccelarators.ifind.exception.UnauthorizedException;
import com.techaccelarators.ifind.repository.RoleRepository;
import com.techaccelarators.ifind.repository.UserAccountRepository;
import com.techaccelarators.ifind.repository.UserRoleRepository;
import com.techaccelarators.ifind.security.JwtTokenProvider;
import com.techaccelarators.ifind.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final JwtTokenProvider tokenProvider;
    private final OtpService otpService;
    private final JavaMailSender emailSender;
    @Override
    public JwtAuthResponse authenticateUser(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword());
        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            String token = tokenProvider.createToken(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new JwtAuthResponse(token);
        } catch (AuthenticationException exception) {
            throw new UnauthorizedException("Unauthorized");
        }
    }

    @Override
    public void registerUser(SignUpDto signUpDto) {
        if(userAccountRepository.existsByUsername(signUpDto.getUsername())){
            throw new InvalidRequestException("Username Already In Use");
        }
        if(userAccountRepository.existsByEmail(signUpDto.getEmail())){
            throw new InvalidRequestException("Email Already In Use");
        }
        UserAccount userAccount = new UserAccount();
        userAccount.setFirstName(signUpDto.getFirstName());
        userAccount.setLastName(signUpDto.getLastName());
        userAccount.setUsername(signUpDto.getUsername());
        userAccount.setEmail(signUpDto.getEmail());
        userAccount.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        userAccount.setStatus(Status.ACTIVE);
        userAccount.setIsOtpRequired(true);

        Role roles = roleRepository.findByName("ROLE_USER").get();
        UserRole userRole = new UserRole();
        userRole.setUserAccount(userAccount);
        userRole.setRole(roles);
        userRoleRepository.save(userRole);

        userAccountRepository.save(userAccount);
    }
    @Override
    @Transactional
    public void forgotPassword(String email, HttpServletRequest request) {
        UserAccount user = userAccountRepository.findByEmail(email)
                .orElseThrow(()-> new RecordNotFoundException(String.format("No user with email %s was found",email)));
        user.setPasswordResetToken(UUID.randomUUID().toString());
        userAccountRepository.save(user);
        String appUrl = request.getScheme() + "://" + request.getServerName();

        SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
        passwordResetEmail.setFrom("${spring.mail.username}");
        passwordResetEmail.setTo(user.getEmail());
        passwordResetEmail.setSubject("Password Reset Request");
        passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl
                + "/reset?token=" + user.getPasswordResetToken());
        try {
            emailSender.send(passwordResetEmail);
        }catch (MailException exception){
           throw new InternalServerErrorException(exception.getMessage());
        }

    }

    @Override
    public JwtAuthResponse verifyOtp(VerifyTokenRequestDTO verifyTokenRequest) {
        String username = verifyTokenRequest.getUsername();
        Integer otp = verifyTokenRequest.getOtp();
        boolean isOtpValid = otpService.validateOTP(username, otp);
        if (!isOtpValid) {
            throw new UnauthorizedException("Unauthorized");
        }
        String token = tokenProvider.createTokenAfterVerifiedOtp(username);
        return new JwtAuthResponse(token);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        UserAccount user = userAccountRepository.findByPasswordResetToken(token)
                .orElseThrow(()-> new RecordNotFoundException("No User with provided reset token was found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        userAccountRepository.save(user);


    }
}
