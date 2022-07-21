package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.CustomerType;
import com.techaccelarators.ifind.dtos.customer.CustomerTypeRequest;
import com.techaccelarators.ifind.service.CustomerTypeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerTypeImpl implements CustomerTypeService {
    @Override
    public CustomerType createCustomerType(CustomerTypeRequest customerTypeRequest) {
        return null;
    }

    @Override
    public CustomerType updateCustomerType(Long id, CustomerTypeRequest customerTypeRequest) {
        return null;
    }

    @Override
    public Page<CustomerType> getAllCustomerTypes(Pageable pageable) {
        return null;
    }

    @Override
    public CustomerType getCustomerTypeById(Long id) {
        return null;
    }

    @Override
    public CustomerType getCustomerTypeByName(String name) {
        return null;
    }

    @Override
    public Page<CustomerType> searchCustomerType(String searchParam, Pageable pageable) {
        return null;
    }

    @Override
    public CustomerType toggleCustomerTypeStatus(Long id) {
        return null;
    }

    @Override
    public void deleteCity(Long id) {

    }
}
