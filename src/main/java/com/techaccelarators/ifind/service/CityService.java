package com.techaccelarators.ifind.service;

import com.techaccelarators.ifind.domain.City;
import com.techaccelarators.ifind.dtos.city.CityRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityService {
    void deleteCity(Long id);

    City toggleCityStatus(Long id);

    Page<City> searchCity(String searchParam, Pageable pageable);

    City getCityByName(String name);

    City getCityById(Long id);

    Page<City> getAllCities(Pageable pageable);

    City updateCity(Long id, CityRequestDto cityRequestDto);

    City createCity(CityRequestDto cityRequestDto);
}
