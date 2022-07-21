package com.techaccelarators.ifind.dtos.customer;

import com.techaccelarators.ifind.domain.Bank;
import com.techaccelarators.ifind.domain.Customer;
import com.techaccelarators.ifind.domain.CustomerType;
import com.techaccelarators.ifind.domain.ServiceType;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.domain.util.Address;
import com.techaccelarators.ifind.domain.util.ContactDetails;
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

    private String description;

    private Address address;

    private CustomerType customerType;

    private String imageUrl;

    private Bank bank;

    private Long accountNumber;

    private ContactDetails contactDetails;

    private Status status;

    private ServiceType serviceType;

    public static CustomerDto of(Customer customer) {
        if (Objects.isNull(customer)) {
            return null;
        }
        return new CustomerDto(customer.getId(), customer.getName(),
                customer.getDescription(), customer.getAddress(), customer.getCustomerType(), customer.getImageUrl(),
                customer.getBank(), customer.getAccountNumber(),
                customer.getContactDetails(), customer.getStatus(), customer.getServiceType());
    }

    public static List<CustomerDto> of(List<Customer> customers) {
        if(Objects.isNull(customers)){
            return Collections.emptyList();
        }
        return customers.stream().map(CustomerDto::of).collect(Collectors.toList());
    }
}
