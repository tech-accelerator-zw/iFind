package com.techaccelarators.ifind.repository;

import com.techaccelarators.ifind.domain.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank,Long> {
    Optional<Bank> findByNameIgnoreCase(String name);

    boolean existsByName(String name);

    Optional<Bank> findBankByName(String name);

    Page<Bank> findAllByNameLikeIgnoreCase(String searchWord, Pageable pageable);
}
