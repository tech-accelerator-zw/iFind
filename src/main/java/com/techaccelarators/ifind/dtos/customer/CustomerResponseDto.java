package com.techaccelarators.ifind.dtos.customer;

import com.techaccelarators.ifind.domain.*;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.domain.util.Address;
import com.techaccelarators.ifind.domain.util.ContactDetails;
import com.techaccelarators.ifind.dtos.branch.CustomerBranchDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDto {
    private Long id;

    private String name;

    private String description;

    private Address address;

    private CustomerType customerType;

    private String imageUrl;

    private Bank bank;

    private Long accountNumber;

    private ContactDetails contactDetails;

    private Status status;

    private ServiceType serviceType;

    private Set<CustomerBranchDto> customerBranchSet;

    public static CustomerResponseDto of(Customer customer, Set<CustomerBranch> customerBranches) {
        if (Objects.isNull(customer)) {
            return null;
        }
        return new CustomerResponseDto(customer.getId(), customer.getName(),
                customer.getDescription(),customer.getAddress(), customer.getCustomerType(), customer.getImageUrl(),
                customer.getBank(), customer.getAccountNumber(),
                customer.getContactDetails(), customer.getStatus(), customer.getServiceType(),customerBranches
                .stream().map(CustomerBranchDto::of).collect(Collectors.toSet()));
    }

}
