package com.techaccelarators.ifind.dtos.city;

import com.techaccelarators.ifind.domain.City;
import com.techaccelarators.ifind.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDto {
    private Long id;

    private String name;

    private Status status;

    public static CityDto of(City city) {
        if (Objects.isNull(city)) {
            return null;
        }
        return new CityDto(city.getId(), city.getName(),city.getStatus());
    }

    public static List<CityDto> of(List<City> cities) {
        if(Objects.isNull(cities)){
            return Collections.emptyList();
        }
        return cities.stream().map(CityDto::of).collect(Collectors.toList());
    }
}
