package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.Role;
import com.techaccelarators.ifind.domain.UserAccount;
import com.techaccelarators.ifind.domain.UserRole;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.security.LoginDto;
import com.techaccelarators.ifind.dtos.security.SignUpDto;
import com.techaccelarators.ifind.exception.InvalidRequestException;
import com.techaccelarators.ifind.repository.RoleRepository;
import com.techaccelarators.ifind.repository.UserAccountRepository;
import com.techaccelarators.ifind.repository.UserRoleRepository;
import com.techaccelarators.ifind.security.JwtTokenProvider;
import com.techaccelarators.ifind.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final JwtTokenProvider tokenProvider;


    @Override
    public String authenticateUser(LoginDto loginDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(),loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
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
        userAccount.setName(signUpDto.getName());
        userAccount.setUsername(signUpDto.getUsername());
        userAccount.setEmail(signUpDto.getEmail());
        userAccount.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        userAccount.setStatus(Status.ACTIVE);

        Role roles = roleRepository.findByName("ROLE_USER").get();
        UserRole userRole = new UserRole();
        userRole.setUserAccount(userAccount);
        userRole.setRole(roles);
        userRoleRepository.save(userRole);

        userAccountRepository.save(userAccount);
    }
}
