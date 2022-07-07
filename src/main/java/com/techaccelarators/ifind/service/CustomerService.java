package com.techaccelarators.ifind.service;

import com.techaccelarators.ifind.domain.Customer;
import com.techaccelarators.ifind.dtos.customer.CustomerRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    Customer createCustomer(CustomerRequest customerRequest);

    Customer updateCustomer(Long id, CustomerRequest customerRequest);

    Customer getCustomerById(Long id);

    Page<Customer> getAllCustomers(Pageable pageable);

    Page<Customer> getAllActiveCustomers(Pageable pageable);

    Customer toggleCustomerStatus(Long id);

}
