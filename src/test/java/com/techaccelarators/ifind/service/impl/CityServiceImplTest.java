package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.Bank;
import com.techaccelarators.ifind.domain.City;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.city.CityRequestDto;
import com.techaccelarators.ifind.exception.InvalidRequestException;
import com.techaccelarators.ifind.exception.RecordNotFoundException;
import com.techaccelarators.ifind.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceImplTest {
    @Mock
    private CityRepository cityRepository;
    @Mock
    private Pageable pageable;
    @InjectMocks
    private CityServiceImpl cityService;
    private City city;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        this.cityService = new CityServiceImpl(cityRepository);

        CityRequestDto cityRequestDto = new CityRequestDto();
        cityRequestDto.setName("Harare");

        city = new City("TestCity");
        city.setId(1L);
        city.setStatus(Status.ACTIVE);
    }

    @Test
    void deleteCitySuccess() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("user","password",
                List.of(new SimpleGrantedAuthority("ADMIN")));
    }

    @Test
    void toggleCityStatusSuccess() {
        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(city));
        when(cityRepository.save(any(City.class))).thenReturn(city);

        City cityResult = cityService.toggleCityStatus(anyLong());

        verify(cityRepository, times(1)).findById(anyLong());
        verify(cityRepository, times(1)).save(any(City.class));
        assertEquals(Status.INACTIVE, cityResult.getStatus());

        cityResult = cityService.toggleCityStatus(anyLong());

        verify(cityRepository, times(2)).findById(anyLong());
        verify(cityRepository, times(2)).save(any(City.class));
        assertEquals(Status.ACTIVE, cityResult.getStatus());
    }
    @Test
    void toggleCityStatusWithInvalidId(){
        when(cityRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> cityService.toggleCityStatus(1L));
        verify(cityRepository, times(1)).findById(anyLong());
        verify(cityRepository, never()).save(any(City.class));
    }

    @Test
    void searchCitySuccess() {
        String searchParam = "city";
        Page<City> expectedBankPage = new PageImpl<>(Collections.singletonList(city));

        when(cityRepository.findAllByNameLikeIgnoreCase("%city%", pageable)).thenReturn(expectedBankPage);

        Page<City> actualBankPage = cityService.searchCity(searchParam, pageable);

        assertEquals(expectedBankPage, actualBankPage);
        verify(cityRepository).findAllByNameLikeIgnoreCase("%city%", pageable);
    }
    @Test
    void searchCityThrowsInvalidRequestException(){
        String searchParam = "invalid-bank";

        when(cityRepository.findAllByNameLikeIgnoreCase("%invalid-bank%", pageable))
                .thenThrow(new InvalidRequestException("Invalid bank"));

        assertThrows(InvalidRequestException.class, () -> cityService.searchCity(searchParam, pageable));
        verify(cityRepository).findAllByNameLikeIgnoreCase("%invalid-bank%", pageable);
    }

    @Test
    void getCityByNameSuccess() {
        when(cityRepository.findCityByName(anyString())).thenReturn(Optional.of(city));

        City retrievedCity = cityService.getCityByName("Harare");

        assertEquals(city, retrievedCity);
        verify(cityRepository, times(1)).findCityByName(anyString());
    }

    @Test
    void getCityByNameThrowsRecordNotFoundException(){
        when(cityRepository.findCityByName(anyString())).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,()-> cityService.getCityByName(anyString()));
        verify(cityRepository, times(1)).findCityByName(anyString());
    }

    @Test
    void getCityByIdSuccess() {
        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(city));

        City retrievedCity = cityService.getCityById(anyLong());

        assertEquals(city, retrievedCity);
        verify(cityRepository, times(1)).findById(anyLong());
    }
    @Test
    void getCityByIdThrowsRecordNotFoundException(){
        when(cityRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,()-> cityService.getCityById(anyLong()));
        verify(cityRepository, times(1)).findById(anyLong());
    }

    @Test
    void getAllCities() {
    }

    @Test
    void updateCity() {
    }

    @Test
    void createCity() {
    }
}