package com.techaccelarators.ifind.dtos.customer;

import com.techaccelarators.ifind.domain.Customer;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.domain.util.Address;
import com.techaccelarators.ifind.domain.util.BankingDetails;
import com.techaccelarators.ifind.domain.util.ContactDetails;
import com.techaccelarators.ifind.domain.util.CustomerType;
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
public class CustomerDto {
    private Long id;

    private String name;

    private Address address;

    private CustomerType customerType;

    private String imageUrl;

    private BankingDetails bankingDetails;

    private ContactDetails contactDetails;

    private Status status;

    public static CustomerDto of(Customer customer) {
        if (Objects.isNull(customer)) {
            return null;
        }
        return new CustomerDto(customer.getId(), customer.getName(),
                customer.getAddress(), customer.getCustomerType(), customer.getImageUrl(), customer.getBankingDetails(),
                customer.getContactDetails(), customer.getStatus());
    }

    public static List<CustomerDto> of(List<Customer> customers) {
        if(Objects.isNull(customers)){
            return Collections.emptyList();
        }
        return customers.stream().map(CustomerDto::of).collect(Collectors.toList());
    }
}
