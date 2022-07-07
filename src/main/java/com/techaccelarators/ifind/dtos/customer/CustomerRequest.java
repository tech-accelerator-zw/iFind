package com.techaccelarators.ifind.dtos.customer;

import com.techaccelarators.ifind.domain.util.Address;
import com.techaccelarators.ifind.domain.util.BankingDetails;
import com.techaccelarators.ifind.domain.util.ContactDetails;
import com.techaccelarators.ifind.domain.util.CustomerType;
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

    @Valid
    private Address address;

    private CustomerType customerType;

    private String imageUrl;

    @NotNull(message = "Banking details are required")
    private BankingDetails bankingDetails;

    @NotNull(message = "Contact Details are required")
    private ContactDetails contactDetails;
}
