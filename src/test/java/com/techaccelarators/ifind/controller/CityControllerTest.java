package com.techaccelarators.ifind.controller;

import com.techaccelarators.ifind.domain.Bank;
import com.techaccelarators.ifind.domain.City;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.bank.BankDto;
import com.techaccelarators.ifind.dtos.city.CityDto;
import com.techaccelarators.ifind.dtos.city.CityRequestDto;
import com.techaccelarators.ifind.exception.ForbiddenException;
import com.techaccelarators.ifind.exception.InvalidRequestException;
import com.techaccelarators.ifind.exception.RecordNotFoundException;
import com.techaccelarators.ifind.exception.UnauthorizedException;
import com.techaccelarators.ifind.service.CityService;
import com.techaccelarators.ifind.util.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CityControllerTest {
    @Mock
    private CityService cityService;
    @Mock
    private Pageable pageable;
    @InjectMocks
    private CityController cityController;

    @BeforeAll
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void injectMocks(){
        this.cityController = new CityController(cityService);
    }

    City RECORD_1 = new City("Harare",Status.ACTIVE);
    City RECORD_2 = new City("Mutare",Status.ACTIVE);
    City RECORD_3 = new City("Bulawayo",Status.ACTIVE);

    @Test
    @DisplayName("createCitySuccess")
    void createCity() {
        CityRequestDto cityRequestDto = new CityRequestDto("Kwekwe");
        City city = new City();
        city.setName(cityRequestDto.getName());

        when(cityService.createCity(any(CityRequestDto.class))).thenReturn(city);

        Response<CityDto> response = cityController.createCity(cityRequestDto);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals("City Created Successfully",response.getMessage());
        assertEquals(3L, response.getResult().getId());
        assertEquals("Kwekwe",response.getResult().getName());
    }

    @Test
    @DisplayName("updateCitySuccess")
    void updateCity() {
        CityRequestDto cityRequestDto = new CityRequestDto();
        City city = new City();
        when(cityService.updateCity(city.getId(),cityRequestDto)).thenReturn(city);

        Response<CityDto> response = cityController.updateCity(city.getId(),cityRequestDto);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("City Updated Successfully", response.getMessage());
        assertNotNull(response.getResult());
        assertEquals(1L, response.getResult().getId());
    }

    @Test
    @DisplayName("getAllCitiesSuccess")
    void getAllCitiesSuccess() {
        Pageable pageable = PageRequest.of(0,10);
        Page<City> cities = new PageImpl<>(Arrays.asList(RECORD_1,RECORD_2,RECORD_3),pageable,3L);
        when(cityService.getAllCities(pageable)).thenReturn(cities);

        Response<Page<CityDto>> response = cityController.getAllCities(pageable);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("SUCCESS",response.getMessage());
        assertEquals(3,response.getResult().getTotalElements());
        assertEquals(3,response.getResult().getContent().size());
    }
    @Test
    @DisplayName("getAllCitiesReturnsEmptyPage")
    void getAllCitiesReturnsEmptyPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<City> cities = new PageImpl<>(Collections.emptyList(),pageable,0);
        when(cityService.getAllCities(pageable)).thenReturn(cities);

        Response<Page<CityDto>> response = cityController.getAllCities(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(0, response.getResult().getTotalElements());
        assertEquals(Collections.emptyList(), response.getResult().getContent());
    }

    @Test
    @DisplayName("getCityByIdSuccess")
    void getCityByIdSuccess() {

        when(cityService.getCityById(RECORD_1.getId())).thenReturn(RECORD_1);
        Response<CityDto> response = cityController.getCityById(1L);

        assertEquals(HttpStatus.FOUND,response.getStatusCode());
        assertEquals("FOUND",response.getMessage());
        assertEquals(1L,response.getResult().getId());
    }
    @Test
    @DisplayName("getCityByIdThrowsRecordNotFound")
    void getCityByIdThrowsRecordNotFound(){
        Long id = 3L;
        when(cityService.getCityById(id)).thenThrow(new RecordNotFoundException("City Not Found"));
        assertThrows(RecordNotFoundException.class,()-> cityController.getCityById(id));
        verify(cityService).getCityById(id);
    }
    @Test
    @DisplayName("getCityByIdThrowsInternalServerError")
    void getCityByIdThrowsInternalServerError(){
        Long id = 3L;
        when(cityService.getCityById(id)).thenThrow(new RuntimeException("Something went wrong"));
        assertThrows(RuntimeException.class,()-> cityController.getCityById(id));
        verify(cityService).getCityById(id);
    }

    @Test
    @DisplayName("getByCityNameSuccess")
    void getCityByNameSuccess() {

        when(cityService.getCityByName("Harare")).thenReturn(RECORD_1);
        Response<CityDto> response = cityController.getCityByName("Harare");

        assertEquals(HttpStatus.FOUND,response.getStatusCode());
        assertEquals("FOUND",response.getMessage());
        assertEquals("Harare",response.getResult().getName());
    }
    @Test
    @DisplayName("getCityByNameThrowsRecordNotFoundException")
    void getCityByNonExistingName(){
        String name = "Non Existing Name";
        when(cityService.getCityByName(name)).thenThrow(new RecordNotFoundException("City Not Found"));
        assertThrows(RecordNotFoundException.class,()-> cityController.getCityByName(name));
        verify(cityService).getCityByName(name);
    }
    @Test
    @DisplayName("getCityByNameThrowsInternalServerError")
    void getCityByNameThrowsInternalServerError(){
        String name = "city";
        when(cityService.getCityByName(name)).thenThrow(new RuntimeException("Something went wrong"));
        assertThrows(RuntimeException.class,()-> cityController.getCityByName(name));
        verify(cityService).getCityByName(name);
    }

    @Test
    @DisplayName("searchCitySuccess")
    void searchCity() {
        String searchParam = "Harare";

        Page<City> cities = new PageImpl<>(Collections.singletonList(RECORD_1),pageable,1L);
        when(cityService.searchCity(searchParam,pageable)).thenReturn(cities);

        Response<Page<CityDto>> response = cityController.searchCity(searchParam,pageable);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("SUCCESS",response.getMessage());
        assertEquals(searchParam, response.getResult().getContent().get(0).getName());
    }
    @Test
    @DisplayName("searchCityThrowsInvalidRequestException")
    void searchCityThrowsInvalidRequest(){
        String searchParam = "Harare";
        when(cityService.searchCity(searchParam,pageable)).thenThrow(new InvalidRequestException("Invalid Request"));

        assertThrows(InvalidRequestException.class,()-> cityController.searchCity(searchParam,pageable));
        verify(cityService).searchCity(searchParam,pageable);
    }
    @Test
    @DisplayName("searchCityThrowsInternalServerError")
    void searchCityThrowsInternalServerError(){
        String searchParam = "Harare";
        when(cityService.searchCity(searchParam,pageable)).thenThrow(new RuntimeException("Something went wrong"));

        assertThrows(RuntimeException.class,()-> cityController.searchCity(searchParam,pageable));
        verify(cityService).searchCity(searchParam,pageable);
    }

    @Test
    @DisplayName("toggleCityStatus")
    void toggleCityStatus() {
        RECORD_1.setStatus(Status.ACTIVE);

        when(cityService.toggleCityStatus(1L)).thenReturn(RECORD_1);

        Response<CityDto> response = cityController.toggleCityStatus(RECORD_1.getId());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("City Status Updated Successfully", response.getMessage());
        assertNotNull(response.getResult());
        assertEquals(RECORD_1.getId(), response.getResult().getId());
        assertEquals(RECORD_1.getStatus(), response.getResult().getStatus());
    }

    @Test
    @DisplayName("deleteCitySuccess")
    void deleteCity() {
        // Arrange
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("username","password",authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Act
        Response<?> response = cityController.deleteCity(RECORD_1.getId());
        // Assert
        verify(cityService).deleteCity(RECORD_1.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("City Deleted Successfully", response.getMessage());

    }
    @Test
    @DisplayName("deleteCityForbidden")
    void deleteCityForbidden() {
        // Arrange
        Long id = 2L;
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("username","password",authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Act
        doThrow(ForbiddenException.class).when(cityService).deleteCity(id);
        assertThrows(ForbiddenException.class,()-> cityController.deleteCity(id));
        verify(cityService).deleteCity(id);

    }
    @Test
    @DisplayName("deleteCityUnauthorized")
    void deleteCityUnauthorized(){
        //Arrange
        Long id = 3L;
        SecurityContextHolder.getContext().setAuthentication(null);
        //Act
        doThrow(UnauthorizedException.class).when(cityService).deleteCity(id);
        assertThrows(UnauthorizedException.class,()-> cityController.deleteCity(id));
        verify(cityService).deleteCity(id);
    }
}