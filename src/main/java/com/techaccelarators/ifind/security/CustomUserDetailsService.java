package com.techaccelarators.ifind.security;

import com.techaccelarators.ifind.domain.UserAccount;
import com.techaccelarators.ifind.domain.UserRole;
import com.techaccelarators.ifind.repository.UserAccountRepository;
import com.techaccelarators.ifind.repository.UserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserAccountRepository userAccountRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail)
                            .orElseThrow(()-> new UsernameNotFoundException("User not found with username or email: "+usernameOrEmail));
        List<UserRole> roles = userRoleRepository.findAllByUserAccount(userAccount);

        return new org.springframework.security.core.userdetails.User(userAccount.getEmail(),
                userAccount.getPassword(), roles.stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getName()))
                .collect(Collectors.toSet()));
    }

}
