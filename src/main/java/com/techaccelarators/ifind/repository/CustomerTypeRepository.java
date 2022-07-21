package com.techaccelarators.ifind.repository;

import com.techaccelarators.ifind.domain.CustomerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerTypeRepository extends JpaRepository<CustomerType,Long> {
    Optional<CustomerType> findByNameIgnoreCase(String name);

    boolean existsByName(String name);

    Optional<CustomerType> findCustomerTypeByName(String name);

    Page<CustomerType> findAllByNameLikeIgnoreCase(String searchWord, Pageable pageable);
}
