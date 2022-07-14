package com.techaccelarators.ifind.service;

import com.techaccelarators.ifind.domain.CustomerBranch;
import com.techaccelarators.ifind.dtos.branch.CustomerBranchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerBranchService {
    CustomerBranch createCustomerBranch(CustomerBranchRequest customerBranchRequest);

    CustomerBranch updateCustomerBranch(Long id, CustomerBranchRequest customerBranchRequest);

    CustomerBranch getCustomerBranchById(Long id);

    Page<CustomerBranch> getAllActiveCustomerBranches(Pageable pageable);

    CustomerBranch toggleCustomerBranchStatus(Long id);
}
