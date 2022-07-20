package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.Customer;
import com.techaccelarators.ifind.domain.CustomerBranch;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.branch.CustomerBranchRequest;
import com.techaccelarators.ifind.exception.InvalidRequestException;
import com.techaccelarators.ifind.exception.RecordNotFoundException;
import com.techaccelarators.ifind.repository.CustomerBranchRepository;
import com.techaccelarators.ifind.repository.CustomerRepository;
import com.techaccelarators.ifind.service.CustomerBranchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerBranchServiceImpl implements CustomerBranchService {

    private final CustomerBranchRepository customerBranchRepository;
    private final CustomerRepository customerRepository;
    @Override
    public CustomerBranch createCustomerBranch(Long customerId,CustomerBranchRequest customerBranchRequest) {

        checkUnique(customerBranchRequest, null);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()-> new RecordNotFoundException("Customer Not Found"));
        CustomerBranch customerBranch = CustomerBranch.builder()
                .name(customerBranchRequest.getName())
                .address(customerBranchRequest.getAddress())
                .contactDetails(customerBranchRequest.getContactDetails())
                .build();
        customerBranch.setStatus(Status.ACTIVE);
        Set<CustomerBranch> customerBranches = customer.getCustomerBranches();
        customerBranches.add(customerBranch);
        customer.setCustomerBranches(customerBranches);
        customerRepository.save(customer);
        return customerBranchRepository.save(customerBranch);
    }

    @Override
    public CustomerBranch updateCustomerBranch(Long customerId, Long branchId, CustomerBranchRequest customerBranchRequest) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()-> new RecordNotFoundException("Customer Not Found"));
        CustomerBranch customerBranch = getCustomerBranchById(branchId);
        customerBranch.setName(customerBranchRequest.getName());
        customerBranch.setAddress(customerBranchRequest.getAddress());
        customerBranch.setContactDetails(customerBranchRequest.getContactDetails());

        Set<CustomerBranch> customerBranches = customer.getCustomerBranches();
        customerBranches.add(customerBranch);
        customer.setCustomerBranches(customerBranches);
        customerRepository.save(customer);

        return customerBranchRepository.save(customerBranch);
    }

    @Override
    public CustomerBranch getCustomerBranchById(Long id) {
        return customerBranchRepository.findById(id)
                .orElseThrow(()-> new RecordNotFoundException("Customer Branch Not Found!!"));
    }

    @Override
    public Page<CustomerBranch> getAllActiveCustomerBranches(Long customerId,Pageable pageable) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()-> new RecordNotFoundException("Customer Not Found"));
        List<CustomerBranch> branchList = customer.getCustomerBranches().stream()
                                            .filter(customerBranch -> customerBranch.getStatus().equals(Status.ACTIVE))
                                            .collect(Collectors.toList());
        return new PageImpl<>(branchList);
    }

    @Override
    public CustomerBranch toggleCustomerBranchStatus(Long id) {
        CustomerBranch customerBranch = getCustomerBranchById(id);

        if (customerBranch.getStatus() == Status.ACTIVE) {
            customerBranch.setStatus(Status.INACTIVE);
        } else if (customerBranch.getStatus() == Status.INACTIVE) {
            customerBranch.setStatus(Status.ACTIVE);
        }
        return customerBranchRepository.save(customerBranch);
    }

    @Override
    public void deleteCustomerBranch(Long id) {
        CustomerBranch customerBranch = getCustomerBranchById(id);
        customerBranchRepository.delete(customerBranch);
    }

    private void checkUnique(CustomerBranchRequest request, Long id) {
        log.info("Checking customer uniqueness with name: {} and id: {}", request.getName(), id);
        customerBranchRepository.findByNameIgnoreCase(request.getName())
                .filter(customerBranch -> !customerBranch.getId().equals(id))
                .ifPresent(customerBranch -> {
                    throw new InvalidRequestException("Customer Branch with given name already exists");
                });
    }
}
