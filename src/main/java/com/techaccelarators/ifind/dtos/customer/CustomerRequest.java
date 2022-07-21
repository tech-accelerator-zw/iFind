package com.techaccelarators.ifind.dtos.customer;

import com.techaccelarators.ifind.domain.util.Address;
import com.techaccelarators.ifind.domain.util.BankingDetails;
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

    private Long bankId;

    private Long accountNumber;

    @NotNull(message = "Contact Details are required")
    private ContactDetails contactDetails;

    @NotNull(message = "Service Type Id is required")
    private Long serviceTypeId;
}
