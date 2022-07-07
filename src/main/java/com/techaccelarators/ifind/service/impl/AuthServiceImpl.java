package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.Role;
import com.techaccelarators.ifind.domain.User;
import com.techaccelarators.ifind.domain.UserRole;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.security.LoginDto;
import com.techaccelarators.ifind.dtos.security.SignUpDto;
import com.techaccelarators.ifind.exception.InvalidRequestException;
import com.techaccelarators.ifind.repository.RoleRepository;
import com.techaccelarators.ifind.repository.UserRepository;
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
    private final UserRepository userRepository;
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
        if(userRepository.existsByUsername(signUpDto.getUsername())){
            throw new InvalidRequestException("Username Already In Use");
        }
        if(userRepository.existsByEmail(signUpDto.getEmail())){
            throw new InvalidRequestException("Email Already In Use");
        }
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setStatus(Status.ACTIVE);

        Role roles = roleRepository.findByName("ROLE_USER").get();
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(roles);
        userRoleRepository.save(userRole);

        userRepository.save(user);
    }
}
