package com.techaccelarators.ifind.repository;

import com.techaccelarators.ifind.domain.Customer;
import com.techaccelarators.ifind.domain.CustomerBranch;
import com.techaccelarators.ifind.domain.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface CustomerBranchRepository extends JpaRepository<CustomerBranch,Long> {
    List<CustomerBranch> findAllByCustomer(Customer customer);

    Optional<CustomerBranch> findByNameIgnoreCase(String name);

    Page<CustomerBranch> findByStatus(Status status, Pageable pageable);
}
