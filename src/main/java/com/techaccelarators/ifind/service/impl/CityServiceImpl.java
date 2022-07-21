package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.City;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.city.CityRequestDto;
import com.techaccelarators.ifind.exception.InvalidRequestException;
import com.techaccelarators.ifind.exception.RecordNotFoundException;
import com.techaccelarators.ifind.repository.CityRepository;
import com.techaccelarators.ifind.service.CityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    @Override
    public void deleteCity(Long id) {
        City city = getCityById(id);
        cityRepository.delete(city);
    }

    @Override
    public City toggleCityStatus(Long id) {
        City city = getCityById(id);

        if (city.getStatus() == Status.ACTIVE) {
            city.setStatus(Status.INACTIVE);
        } else if (city.getStatus() == Status.INACTIVE) {
            city.setStatus(Status.ACTIVE);
        }
        return cityRepository.save(city);
    }

    @Override
    public Page<City> searchCity(String searchParam, Pageable pageable) {
        try {
            String searchWord = "%".concat(searchParam).concat("%");
            return cityRepository.findAllByNameLikeIgnoreCase(searchWord, pageable);
        } catch (Exception ex) {
            throw new InvalidRequestException(ex.getMessage());
        }
    }

    @Override
    public City getCityByName(String name) {
        return cityRepository.findCityByName(name)
                .orElseThrow(()-> new RecordNotFoundException("City Not Found"));
    }

    @Override
    public City getCityById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(()-> new RecordNotFoundException("City Not Found"));
    }

    @Override
    public Page<City> getAllCities(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    @Override
    public City updateCity(Long id, CityRequestDto cityRequestDto) {
        City city = getCityById(id);
        checkUnique(cityRequestDto, id);
        city.setName(cityRequestDto.getName());
        if(cityRepository.existsByName(cityRequestDto.getName())){
            throw new InvalidRequestException("Bank Name Already In Use");
        }

        return cityRepository.save(city);
    }

    @Override
    public City createCity(CityRequestDto cityRequestDto) {
        checkUnique(cityRequestDto, null);
        City city = City.builder()
                .name(cityRequestDto.getName())
                .build();
        city.setStatus(Status.ACTIVE);
        return cityRepository.save(city);
    }

    private void checkUnique(CityRequestDto request, Long id) {
        log.info("Checking city uniqueness with name: {} and id: {}", request.getName(), id);
        cityRepository.findByNameIgnoreCase(request.getName())
                .filter(customer -> !customer.getId().equals(id))
                .ifPresent(customer -> {
                    throw new InvalidRequestException("City with given name already exists");
                });
    }
}
