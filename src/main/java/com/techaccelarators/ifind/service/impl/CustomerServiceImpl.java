package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.*;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.customer.CustomerRequest;
import com.techaccelarators.ifind.dtos.customer.CustomerResponseDto;
import com.techaccelarators.ifind.exception.InvalidRequestException;
import com.techaccelarators.ifind.exception.RecordNotFoundException;
import com.techaccelarators.ifind.repository.CustomerRepository;
import com.techaccelarators.ifind.repository.CustomerServiceRepository;
import com.techaccelarators.ifind.repository.ServiceTypeRepository;
import com.techaccelarators.ifind.service.BankService;
import com.techaccelarators.ifind.service.CustomerService;
import com.techaccelarators.ifind.service.CustomerTypeService;
import com.techaccelarators.ifind.service.ServiceTypeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ServiceTypeService serviceTypeService;
    private final BankService bankService;
    private final CustomerTypeService customerTypeService;
    private final CustomerServiceRepository customerServiceRepository;


    @Override
    public Customer createCustomer(CustomerRequest customerRequest) {

        checkUnique(customerRequest, null);
        CustomerType customerType = customerTypeService.getCustomerTypeById(customerRequest.getCustomerTypeId());
        Bank bank = bankService.getBankById(customerRequest.getBankId());

        ServiceType serviceType = serviceTypeService.getById(customerRequest.getServiceTypeId());

        if(customerRepository.existsByAccountNumberAndBank_Name(customerRequest.getAccountNumber(), bank.getName())){
            throw new InvalidRequestException("Account Number Already In Use");
        }


        Customer customer = Customer.builder()
                .name(customerRequest.getName())
                .description(customerRequest.getDescription())
                .address(customerRequest.getAddress())
                .customerType(customerType)
                .imageUrl(customerRequest.getImageUrl())
                .bank(bank)
                .accountNumber(customerRequest.getAccountNumber())
                .contactDetails(customerRequest.getContactDetails())
                .serviceType(serviceType)
                .build();
        customer.setStatus(Status.ACTIVE);
        return customerRepository.save(customer);

    }

    @Override
    public Customer updateCustomer(Long id, CustomerRequest customerRequest) {

        Customer customer = getById(id);
        CustomerType customerType = customerTypeService.getCustomerTypeById(customerRequest.getCustomerTypeId());
        Bank bank = bankService.getBankById(customerRequest.getBankId());
        ServiceType serviceType = serviceTypeService.getById(customerRequest.getServiceTypeId());

        checkUnique(customerRequest, id);

        customer.setName(customerRequest.getName());
        customer.setAddress(customerRequest.getAddress());
        customer.setCustomerType(customerType);
        customer.setImageUrl(customerRequest.getImageUrl());
        customer.setBank(bank);
        customer.setAccountNumber(customerRequest.getAccountNumber());
        customer.setContactDetails(customerRequest.getContactDetails());
        customer.setServiceType(serviceType);

        if(customerRepository.existsByAccountNumberAndBank_Name(customerRequest.getAccountNumber(), bank.getName())){
            throw new InvalidRequestException("Account Number Already In Use");
        }

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
            return customerRepository.findAllByNameLikeIgnoreCaseOrAddress_CityLikeIgnoreCase(searchWord,searchWord, pageable);
        } catch (Exception ex) {
            throw new InvalidRequestException(ex.getMessage());
        }
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = getById(id);
        customerRepository.delete(customer);
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
        ServiceType serviceType = serviceTypeService.getById(serviceId);
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
