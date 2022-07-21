package com.techaccelarators.ifind.repository;

import com.techaccelarators.ifind.domain.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City,Long> {
    Page<City> findAllByNameLikeIgnoreCase(String searchWord, Pageable pageable);

    Optional<City> findCityByName(String name);

    Optional<City> findByNameIgnoreCase(String name);

    boolean existsByName(String name);
}
