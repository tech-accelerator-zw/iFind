package com.techaccelarators.ifind.repository;

import com.techaccelarators.ifind.domain.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerTypeRepository extends JpaRepository<CustomerType,Long> {
}
