package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.CustomerType;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.customer.CustomerTypeRequest;
import com.techaccelarators.ifind.exception.InvalidRequestException;
import com.techaccelarators.ifind.exception.RecordNotFoundException;
import com.techaccelarators.ifind.repository.CustomerTypeRepository;
import com.techaccelarators.ifind.service.CustomerTypeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerTypeImpl implements CustomerTypeService {
    private final CustomerTypeRepository customerTypeRepository;
    @Override
    public CustomerType createCustomerType(CustomerTypeRequest customerTypeRequest) {
        checkUnique(customerTypeRequest, null);
        CustomerType customerType = CustomerType.builder()
                .name(customerTypeRequest.getName())
                .build();
        customerType.setStatus(Status.ACTIVE);
        return customerTypeRepository.save(customerType);
    }

    @Override
    public CustomerType updateCustomerType(Long id, CustomerTypeRequest customerTypeRequest) {
        CustomerType customerType = getCustomerTypeById(id);
        checkUnique(customerTypeRequest, id);
        customerType.setName(customerTypeRequest.getName());
        if(customerTypeRepository.existsByName(customerTypeRequest.getName())){
            throw new InvalidRequestException("CustomerType Name Already In Use");
        }

        return customerTypeRepository.save(customerType);
    }

    @Override
    public Page<CustomerType> getAllCustomerTypes(Pageable pageable) {
        return customerTypeRepository.findAll(pageable);
    }

    @Override
    public CustomerType getCustomerTypeById(Long id) {
        return customerTypeRepository.findById(id)
                .orElseThrow(()-> new RecordNotFoundException("CustomerType Not Found"));
    }

    @Override
    public CustomerType getCustomerTypeByName(String name) {
        return customerTypeRepository.findCustomerTypeByName(name)
                .orElseThrow(()-> new RecordNotFoundException("CustomerType Not Found"));
    }

    @Override
    public Page<CustomerType> searchCustomerType(String searchParam, Pageable pageable) {
        try {
            String searchWord = "%".concat(searchParam).concat("%");
            return customerTypeRepository.findAllByNameLikeIgnoreCase(searchWord, pageable);
        } catch (Exception ex) {
            throw new InvalidRequestException(ex.getMessage());
        }
    }

    @Override
    public CustomerType toggleCustomerTypeStatus(Long id) {
        CustomerType customerType = getCustomerTypeById(id);

        if (customerType.getStatus() == Status.ACTIVE) {
            customerType.setStatus(Status.INACTIVE);
        } else if (customerType.getStatus() == Status.INACTIVE) {
            customerType.setStatus(Status.ACTIVE);
        }
        return customerTypeRepository.save(customerType);
    }

    @Override
    public void deleteCity(Long id) {
        CustomerType customerType = getCustomerTypeById(id);
        customerTypeRepository.delete(customerType);
    }

    private void checkUnique(CustomerTypeRequest request, Long id) {
        log.info("Checking CustomerType uniqueness with name: {} and id: {}", request.getName(), id);
        customerTypeRepository.findByNameIgnoreCase(request.getName())
                .filter(customer -> !customer.getId().equals(id))
                .ifPresent(customer -> {
                    throw new InvalidRequestException("CustomerType with given name already exists");
                });
    }
}
