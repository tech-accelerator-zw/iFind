package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.City;
import com.techaccelarators.ifind.dtos.city.CityRequestDto;
import com.techaccelarators.ifind.service.CityService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CityServiceImpl implements CityService {
    @Override
    public void deleteCity(Long id) {

    }

    @Override
    public City toggleCityStatus(Long id) {
        return null;
    }

    @Override
    public Page<City> searchCity(String searchParam, Pageable pageable) {
        return null;
    }

    @Override
    public City getCityByName(String name) {
        return null;
    }

    @Override
    public City getCityById(Long id) {
        return null;
    }

    @Override
    public Page<City> getAllCities(Pageable pageable) {
        return null;
    }

    @Override
    public City updateCity(Long id, CityRequestDto cityRequestDto) {
        return null;
    }

    @Override
    public City createCity(CityRequestDto cityRequestDto) {
        return null;
    }
}
