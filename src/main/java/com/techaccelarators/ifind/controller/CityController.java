package com.techaccelarators.ifind.controller;

import com.techaccelarators.ifind.domain.City;
import com.techaccelarators.ifind.dtos.city.CityDto;
import com.techaccelarators.ifind.dtos.city.CityRequestDto;
import com.techaccelarators.ifind.service.CityService;
import com.techaccelarators.ifind.util.Response;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/city")
public class CityController {

    private final CityService cityService;

    @PostMapping
    public Response<CityDto> createCity(@Valid @RequestBody CityRequestDto cityRequestDto) {

        City city = cityService.createCity(cityRequestDto);
        return new Response<CityDto>().buildSuccessResponse("City Created Successfully",
                CityDto.of(city), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "*")
    public Response<CityDto> updateCity(@PathVariable Long id, @Valid @RequestBody CityRequestDto cityRequestDto) {

        City city = cityService.updateCity(id,cityRequestDto);
        return new Response<CityDto>().buildSuccessResponse("City Updated Successfully",
                CityDto.of(city), HttpStatus.OK);
    }

    @GetMapping
    public Response<Page<CityDto>> getAllCities(@PageableDefault Pageable pageable) {

        Page<City> cities = cityService.getAllCities(pageable);
        return new Response<Page<CityDto>>().buildSuccessResponse("SUCCESS",
                new PageImpl<>(CityDto.of(cities.getContent()),pageable, cities.getTotalElements()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Response<CityDto> getCityById(@PathVariable Long id) {
        City city = cityService.getCityById(id);
        return new Response<CityDto>().buildSuccessResponse("FOUND",
                CityDto.of(city), HttpStatus.FOUND);
    }

    @GetMapping("/name")
    public Response<CityDto> getCityByName(@RequestParam String name) {

        City city = cityService.getCityByName(name);
        return new Response<CityDto>().buildSuccessResponse("FOUND",
                CityDto.of(city), HttpStatus.FOUND);
    }

    @GetMapping("/search")
    public Response<Page<CityDto>> searchCity(@RequestParam String searchParam, @PageableDefault Pageable pageable) {

        Page<City> cities = cityService.searchCity(searchParam, pageable);
        return new Response<Page<CityDto>>().buildSuccessResponse("SUCCESSFUL",
                new PageImpl<>(CityDto.of(cities.getContent()),
                        pageable, cities.getTotalElements()),HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    @CrossOrigin(origins = "*")
    public Response<CityDto> toggleCityStatus(@PathVariable Long id) {

        City city = cityService.toggleCityStatus(id);
        return new Response<CityDto>().buildSuccessResponse("City Status Updated Successfully",
                CityDto.of(city), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<?> deleteCity(@PathVariable Long id) {

        cityService.deleteCity(id);
        return new Response<>().buildSuccessResponse("City Deleted Successfully",HttpStatus.OK);
    }
}
