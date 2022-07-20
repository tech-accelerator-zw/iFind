package com.techaccelarators.ifind.repository;

import com.techaccelarators.ifind.domain.ServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceTypeRepository extends JpaRepository<ServiceType,Long> {
    Optional<ServiceType> findServiceTypeByNameIgnoreCase(String name);

    Page<ServiceType> findAllByNameLikeIgnoreCase(String searchWord, Pageable pageable);
}
