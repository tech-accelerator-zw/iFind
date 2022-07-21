package com.techaccelarators.ifind.dtos.city;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityRequestDto {
    @NotEmpty(message = "Name should not be null or empty")
    private String name;
}
