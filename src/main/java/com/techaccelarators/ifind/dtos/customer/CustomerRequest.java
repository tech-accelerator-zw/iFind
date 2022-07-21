package com.techaccelarators.ifind.dtos.customer;

import com.techaccelarators.ifind.domain.util.Address;
import com.techaccelarators.ifind.domain.util.ContactDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    private String description;

    @Valid
    private Address address;

    private Long customerTypeId;

    private String imageUrl;
    @NotNull(message = "Bank id is required")
    private Long bankId;
    @NotNull(message = "Bank account number is required")
    private Long accountNumber;

    @NotNull(message = "Contact Details are required")
    private ContactDetails contactDetails;

    @NotNull(message = "Service Type Id is required")
    private Long serviceTypeId;
}
