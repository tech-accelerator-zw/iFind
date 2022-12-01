package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.UserAccount;
import com.techaccelarators.ifind.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
 @Autowired
 private UserAccountRepository userAccountRepository;

    public String findEmailByUsername(String username) {
        Optional<UserAccount> user = userAccountRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get().getEmail();
        }
        return null;
    }
}
