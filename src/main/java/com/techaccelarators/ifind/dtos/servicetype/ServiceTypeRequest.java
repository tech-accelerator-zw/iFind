package com.techaccelarators.ifind.dtos.servicetype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceTypeRequest {

    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    private String icon;
}
