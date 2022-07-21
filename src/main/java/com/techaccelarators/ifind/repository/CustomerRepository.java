package com.techaccelarators.ifind.repository;

import com.techaccelarators.ifind.domain.Customer;
import com.techaccelarators.ifind.domain.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Optional<Customer> findByNameIgnoreCase(String name);

    Page<Customer> findByStatus(Status status, Pageable pageable);
    Page<Customer> findAllByNameLikeIgnoreCase(String name,Pageable pageable);

    Boolean existsByBankingDetails_AccountNumber(String accountNumber);
}
