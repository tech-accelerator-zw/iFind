package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.Customer;
import com.techaccelarators.ifind.domain.CustomerBranch;
import com.techaccelarators.ifind.domain.ServiceType;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.customer.CustomerRequest;
import com.techaccelarators.ifind.dtos.customer.CustomerResponseDto;
import com.techaccelarators.ifind.exception.InvalidRequestException;
import com.techaccelarators.ifind.exception.RecordNotFoundException;
import com.techaccelarators.ifind.repository.CustomerBranchRepository;
import com.techaccelarators.ifind.repository.CustomerRepository;
import com.techaccelarators.ifind.repository.CustomerServiceRepository;
import com.techaccelarators.ifind.repository.ServiceTypeRepository;
import com.techaccelarators.ifind.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final CustomerServiceRepository customerServiceRepository;


    @Override
    public Customer createCustomer(CustomerRequest customerRequest) {
        checkUnique(customerRequest, null);
        Customer customer = Customer.builder()
                .name(customerRequest.getName())
                .address(customerRequest.getAddress())
                .customerType(customerRequest.getCustomerType())
                .imageUrl(customerRequest.getImageUrl())
                .bankingDetails(customerRequest.getBankingDetails())
                .contactDetails(customerRequest.getContactDetails())
                .build();
        customer.setStatus(Status.ACTIVE);
        return customerRepository.save(customer);

    }

    @Override
    public Customer updateCustomer(Long id, CustomerRequest customerRequest) {

        Customer customer = getById(id);
        checkUnique(customerRequest, id);
        customer.setName(customerRequest.getName());
        customer.setAddress(customerRequest.getAddress());
        customer.setCustomerType(customerRequest.getCustomerType());
        customer.setImageUrl(customerRequest.getImageUrl());
        customer.setBankingDetails(customerRequest.getBankingDetails());
        customer.setContactDetails(customerRequest.getContactDetails());

        return customerRepository.save(customer);

    }
    public Customer getById(Long id){
        return customerRepository.findById(id)
                .orElseThrow(()-> new RecordNotFoundException("Customer Not Found"));
    }

    @Override
    public Page<Customer> searchCustomer(String searchParam, Pageable pageable) {
        try {
            String searchWord = "%".concat(searchParam).concat("%");
            return customerRepository.findAllByServiceTypeLikeIgnoreCaseOrNameLikeIgnoreCase(searchWord, searchWord, pageable);
        } catch (Exception ex) {
            throw new InvalidRequestException(ex.getMessage());
        }
    }

    @Override
    public CustomerResponseDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Customer Not found"));
        Set<CustomerBranch> branches = customer.getCustomerBranches();
        return CustomerResponseDto.of(customer,branches);

    }

    @Override
    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public Page<Customer> getAllActiveCustomers(Pageable pageable) {
        return customerRepository.findByStatus(Status.ACTIVE, pageable);
    }

    @Override
    public Customer toggleCustomerStatus(Long id) {
        Customer customer = getById(id);

        if (customer.getStatus() == Status.ACTIVE) {
            customer.setStatus(Status.INACTIVE);
        } else if (customer.getStatus() == Status.INACTIVE) {
            customer.setStatus(Status.ACTIVE);
        }
        return customerRepository.save(customer);
    }

    @Override
    public void assignCustomerToServiceType(Long customerId, Long serviceId) {
        Customer customer = getById(customerId);
        ServiceType serviceType = serviceTypeRepository.findById(serviceId)
                .orElseThrow(()-> new RecordNotFoundException("ServiceType Not Found!!"));
        com.techaccelarators.ifind.domain.CustomerService customerService = new com.techaccelarators.ifind.domain.CustomerService();
        customerService.setCustomer(customer);
        customerService.setServiceType(serviceType);
        customerService.setStatus(Status.ACTIVE);

        customerServiceRepository.save(customerService);
    }


    private void checkUnique(CustomerRequest request, Long id) {
        log.info("Checking customer uniqueness with name: {} and id: {}", request.getName(), id);
        customerRepository.findByNameIgnoreCase(request.getName())
                .filter(customer -> !customer.getId().equals(id))
                .ifPresent(customer -> {
                    throw new InvalidRequestException("Customer with given name already exists");
                });
    }
}
