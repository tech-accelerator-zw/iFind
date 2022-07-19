package com.techaccelarators.ifind.repository;

import com.techaccelarators.ifind.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount,Long> {
    Optional<UserAccount> findByEmail(String email);
    Optional<UserAccount> findByUsername(String email);
    Optional<UserAccount> findByUsernameOrEmail(String username, String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
