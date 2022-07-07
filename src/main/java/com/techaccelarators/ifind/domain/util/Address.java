package com.techaccelarators.ifind.domain.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @NotBlank(message = "Address Line Is Required")
    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @NotBlank(message = "City Name Is Required")
    @Column(name = "city")
    private String city;

    @NotBlank(message = "Country is Required")
    @Column(name = "country")
    private String country;
}
