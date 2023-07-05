package com.techaccelarators.ifind.service;

import com.techaccelarators.ifind.domain.CustomerType;
import com.techaccelarators.ifind.dtos.customer.CustomerTypeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerTypeService {
    CustomerType createCustomerType(CustomerTypeRequest customerTypeRequest);

    CustomerType updateCustomerType(Long id, CustomerTypeRequest customerTypeRequest);

    Page<CustomerType> getAllCustomerTypes(Pageable pageable);

    CustomerType getCustomerTypeById(Long id);

    CustomerType getCustomerTypeByName(String name);

    Page<CustomerType> searchCustomerType(String searchParam, Pageable pageable);

    CustomerType toggleCustomerTypeStatus(Long id);

    void deleteCustomerType(Long id);
}
