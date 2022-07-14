package com.techaccelarators.ifind.controller;

import com.techaccelarators.ifind.domain.CustomerBranch;
import com.techaccelarators.ifind.dtos.branch.CustomerBranchDto;
import com.techaccelarators.ifind.dtos.branch.CustomerBranchRequest;
import com.techaccelarators.ifind.service.CustomerBranchService;
import com.techaccelarators.ifind.util.Response;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/customer-branch")
public class CustomerBranchController {
    private final CustomerBranchService customerBranchService;

    @PostMapping("{customerId}")
    public Response<CustomerBranchDto> createCustomerBranch(@PathVariable Long customerId,@Valid @RequestBody CustomerBranchRequest customerBranchRequest) {

        CustomerBranch customerBranch = customerBranchService.createCustomerBranch(customerId,customerBranchRequest);
        return new Response<CustomerBranchDto>().buildSuccessResponse("Customer Branch Created Successfully",
                CustomerBranchDto.of(customerBranch), HttpStatus.CREATED);
    }

    @PutMapping("/{customerId}/branch/{branchId}")
    public Response<CustomerBranchDto> updateCustomerBranch(@PathVariable Long customerId,@PathVariable Long branchId,
                                                @Valid @RequestBody CustomerBranchRequest customerBranchRequest) {

        CustomerBranch customerBranch = customerBranchService.updateCustomerBranch(customerId,branchId,customerBranchRequest);
        return new Response<CustomerBranchDto>().buildSuccessResponse("Customer Branch Updated Successfully",
                CustomerBranchDto.of(customerBranch), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Response<CustomerBranchDto> getCustomerBranchById(@PathVariable Long id) {

        CustomerBranch customerBranch = customerBranchService.getCustomerBranchById(id);
        return new Response<CustomerBranchDto>().buildSuccessResponse("FOUND",
                CustomerBranchDto.of(customerBranch), HttpStatus.FOUND);
    }

    @GetMapping("/{customerId}/active")
    public Response<Page<CustomerBranchDto>> getActiveCustomerBranches(@PathVariable Long customerId,@PageableDefault Pageable pageable) {

        Page<CustomerBranch> customerBranches = customerBranchService.getAllActiveCustomerBranches(customerId,pageable);
        return new Response<Page<CustomerBranchDto>>().buildSuccessResponse("FOUND",
                new PageImpl<>(CustomerBranchDto.of(customerBranches.getContent()), pageable, customerBranches.getTotalElements()), HttpStatus.FOUND);
    }

    @PutMapping("/{id}/status")
    public Response<CustomerBranchDto> toggleCustomerStatus(@PathVariable Long id) {

        CustomerBranch customerBranch = customerBranchService.toggleCustomerBranchStatus(id);
        return new Response<CustomerBranchDto>().buildSuccessResponse("Customer Branch Status Updated Successfully",
                CustomerBranchDto.of(customerBranch), HttpStatus.OK);
    }
}
