package com.techaccelarators.ifind.service;

import com.techaccelarators.ifind.domain.Customer;
import com.techaccelarators.ifind.dtos.customer.CustomerRequest;
import com.techaccelarators.ifind.dtos.customer.CustomerResponseDto;
import com.techaccelarators.ifind.exception.ForbiddenException;
import com.techaccelarators.ifind.exception.InternalServerErrorException;
import com.techaccelarators.ifind.exception.UnauthorizedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    Customer createCustomer(CustomerRequest customerRequest);

    Customer updateCustomer(Long id, CustomerRequest customerRequest);

    CustomerResponseDto getCustomerById(Long id);

    Page<Customer> getAllCustomers(Pageable pageable);

    Page<Customer> getAllActiveCustomers(Pageable pageable);

    Customer toggleCustomerStatus(Long id);

    void assignCustomerToServiceType(Long customerId,Long serviceId);

    Customer getById(Long id);

    Page<Customer> searchCustomer(String searchParam, Pageable pageable);

    void deleteCustomer(Long id)throws UnauthorizedException, ForbiddenException, InternalServerErrorException;
}
