package com.techaccelarators.ifind.repository;

import com.techaccelarators.ifind.domain.CustomerService;
import com.techaccelarators.ifind.domain.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerServiceRepository extends JpaRepository<CustomerService,Long> {
    List<CustomerService> findAllByServiceType(ServiceType serviceType);
}
