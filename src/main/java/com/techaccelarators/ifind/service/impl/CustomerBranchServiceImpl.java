package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.CustomerBranch;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.branch.CustomerBranchRequest;
import com.techaccelarators.ifind.exception.InvalidRequestException;
import com.techaccelarators.ifind.exception.RecordNotFoundException;
import com.techaccelarators.ifind.repository.CustomerBranchRepository;
import com.techaccelarators.ifind.service.CustomerBranchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerBranchServiceImpl implements CustomerBranchService {

    private final CustomerBranchRepository customerBranchRepository;
    @Override
    public CustomerBranch createCustomerBranch(CustomerBranchRequest customerBranchRequest) {
        checkUnique(customerBranchRequest, null);
        CustomerBranch customerBranch = CustomerBranch.builder()
                .name(customerBranchRequest.getName())
                .address(customerBranchRequest.getAddress())
                .contactDetails(customerBranchRequest.getContactDetails())
                .build();
        customerBranch.setStatus(Status.ACTIVE);
        return customerBranchRepository.save(customerBranch);
    }

    @Override
    public CustomerBranch updateCustomerBranch(Long id, CustomerBranchRequest customerBranchRequest) {
        CustomerBranch customerBranch = getCustomerBranchById(id);
        checkUnique(customerBranchRequest, id);
        customerBranch.setName(customerBranchRequest.getName());
        customerBranch.setAddress(customerBranchRequest.getAddress());
        customerBranch.setContactDetails(customerBranchRequest.getContactDetails());
        return customerBranchRepository.save(customerBranch);
    }

    @Override
    public CustomerBranch getCustomerBranchById(Long id) {
        return customerBranchRepository.findById(id)
                .orElseThrow(()-> new RecordNotFoundException("Customer Branch Not Found!!"));
    }

    @Override
    public Page<CustomerBranch> getAllActiveCustomerBranches(Pageable pageable) {
        return customerBranchRepository.findByStatus(Status.ACTIVE,pageable);
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
    private void checkUnique(CustomerBranchRequest request, Long id) {
        log.info("Checking customer uniqueness with name: {} and id: {}", request.getName(), id);
        customerBranchRepository.findByNameIgnoreCase(request.getName())
                .filter(customerBranch -> !customerBranch.getId().equals(id))
                .ifPresent(customerBranch -> {
                    throw new InvalidRequestException("Customer Branch with given name already exists");
                });
    }
}
