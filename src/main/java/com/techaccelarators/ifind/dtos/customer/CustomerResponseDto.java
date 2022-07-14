package com.techaccelarators.ifind.dtos.customer;

import com.techaccelarators.ifind.domain.Customer;
import com.techaccelarators.ifind.domain.CustomerBranch;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.domain.util.Address;
import com.techaccelarators.ifind.domain.util.BankingDetails;
import com.techaccelarators.ifind.domain.util.ContactDetails;
import com.techaccelarators.ifind.domain.util.CustomerType;
import com.techaccelarators.ifind.dtos.branch.CustomerBranchDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDto {
    private Long id;

    private String name;

    private Address address;

    private CustomerType customerType;

    private String imageUrl;

    private BankingDetails bankingDetails;

    private ContactDetails contactDetails;

    private Status status;

    private Set<CustomerBranchDto> customerBranchSet;

    public static CustomerResponseDto of(Customer customer, Set<CustomerBranch> customerBranches) {
        if (Objects.isNull(customer)) {
            return null;
        }
        return new CustomerResponseDto(customer.getId(), customer.getName(),
                customer.getAddress(), customer.getCustomerType(), customer.getImageUrl(), customer.getBankingDetails(),
                customer.getContactDetails(), customer.getStatus(),customerBranches
                .stream().map(CustomerBranchDto::of).collect(Collectors.toSet()));
    }

}
