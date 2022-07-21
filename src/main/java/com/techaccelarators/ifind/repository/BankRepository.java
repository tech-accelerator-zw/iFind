package com.techaccelarators.ifind.repository;

import com.techaccelarators.ifind.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank,Long> {
}
