package com.techaccelarators.ifind.service;

import com.techaccelarators.ifind.domain.CustomerBranch;
import com.techaccelarators.ifind.dtos.branch.CustomerBranchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerBranchService {
    CustomerBranch createCustomerBranch(Long customerId,CustomerBranchRequest customerBranchRequest);

    CustomerBranch updateCustomerBranch(Long customerId, Long branchId, CustomerBranchRequest customerBranchRequest);

    CustomerBranch getCustomerBranchById(Long id);

    Page<CustomerBranch> getAllActiveCustomerBranches(Long customerId, Pageable pageable);

    CustomerBranch toggleCustomerBranchStatus(Long id);
}
