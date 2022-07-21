package com.techaccelarators.ifind.controller;

import com.techaccelarators.ifind.domain.Customer;
import com.techaccelarators.ifind.dtos.customer.CustomerDto;
import com.techaccelarators.ifind.dtos.customer.CustomerRequest;
import com.techaccelarators.ifind.dtos.customer.CustomerResponseDto;
import com.techaccelarators.ifind.service.CustomerService;
import com.techaccelarators.ifind.util.Response;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public Response<CustomerDto> createCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        Customer customer = customerService.createCustomer(customerRequest);
        return new Response<CustomerDto>().buildSuccessResponse("Customer Created Successfully",
                CustomerDto.of(customer), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Response<CustomerDto> updateCustomer(@PathVariable Long id,
                                                @Valid @RequestBody CustomerRequest customerRequest) {

        Customer customer = customerService.updateCustomer(id, customerRequest);
        return new Response<CustomerDto>().buildSuccessResponse("Customer Updated Successfully",
                CustomerDto.of(customer), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Response<CustomerResponseDto> getCustomerById(@PathVariable Long id) {

        CustomerResponseDto customer = customerService.getCustomerById(id);
        return new Response<CustomerResponseDto>().buildSuccessResponse("FOUND",
                customer, HttpStatus.FOUND);
    }

    @GetMapping
    public Response<Page<CustomerDto>> getAllCustomers(@PageableDefault Pageable pageable) {

        Page<Customer> customers = customerService.getAllCustomers(pageable);
        return new Response<Page<CustomerDto>>().buildSuccessResponse("FOUND",
                new PageImpl<>(CustomerDto.of(customers.getContent()), pageable, customers.getTotalElements()), HttpStatus.FOUND);
    }

    @GetMapping("/active")
    public Response<Page<CustomerDto>> getActiveCustomers(@PageableDefault Pageable pageable) {

        Page<Customer> customers = customerService.getAllActiveCustomers(pageable);
        return new Response<Page<CustomerDto>>().buildSuccessResponse("FOUND",
                new PageImpl<>(CustomerDto.of(customers.getContent()), pageable, customers.getTotalElements()), HttpStatus.FOUND);
    }
    @GetMapping("/search")
    public Response<Page<CustomerDto>> searchCustomer(@RequestParam String searchParam, @PageableDefault Pageable pageable) {
        Page<Customer> customers = customerService.searchCustomer(searchParam, pageable);
        return new Response<Page<CustomerDto>>().buildSuccessResponse("SUCCESSFUL",
                new PageImpl<>(CustomerDto.of(customers.getContent()),
                        pageable, customers.getTotalElements()),HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public Response<CustomerDto> toggleCustomerStatus(@PathVariable Long id) {

        Customer customer = customerService.toggleCustomerStatus(id);
        return new Response<CustomerDto>().buildSuccessResponse("Customer Status Updated Successfully",
                CustomerDto.of(customer), HttpStatus.OK);
    }
    @PutMapping("/{customerId}/service/{serviceId}")
    public Response<?> assignCustomerToServiceType(@PathVariable Long customerId,@PathVariable Long serviceId){

        customerService.assignCustomerToServiceType(customerId, serviceId);
        return new Response<>().buildSuccessResponse("Assigned Customer to ServiceType",HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<?> deleteCustomer(@PathVariable Long id) {

        customerService.deleteCustomer(id);
        return new Response<>().buildSuccessResponse("Customer Deleted Successfully",HttpStatus.OK);
    }

}
