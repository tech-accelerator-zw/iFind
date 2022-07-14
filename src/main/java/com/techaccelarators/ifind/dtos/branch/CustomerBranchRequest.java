package com.techaccelarators.ifind.dtos.branch;

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
public class CustomerBranchRequest {
    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    @Valid
    private Address address;

    @NotNull(message = "Contact Details are required")
    private ContactDetails contactDetails;
}
