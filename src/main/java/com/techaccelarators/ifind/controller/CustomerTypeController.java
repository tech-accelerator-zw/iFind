package com.techaccelarators.ifind.controller;

import com.techaccelarators.ifind.domain.CustomerType;
import com.techaccelarators.ifind.dtos.customer.CustomerTypeDto;
import com.techaccelarators.ifind.dtos.customer.CustomerTypeRequest;
import com.techaccelarators.ifind.exception.*;
import com.techaccelarators.ifind.service.CustomerTypeService;
import com.techaccelarators.ifind.util.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/v1/customer-type")
@Tag(name = "Customer Type Controller", description = "Rest Controller for Customer Types")
public class CustomerTypeController {

    private final CustomerTypeService customerTypeService;

    @PostMapping
    public Response<CustomerTypeDto> createCustomerType(@Valid @RequestBody CustomerTypeRequest customerTypeRequest) {

        CustomerType customerType = customerTypeService.createCustomerType(customerTypeRequest);
        return new Response<CustomerTypeDto>().buildSuccessResponse("CustomerType Created Successfully",
                CustomerTypeDto.of(customerType), HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public Response<CustomerTypeDto> updateCustomerType(@PathVariable Long id, @Valid @RequestBody CustomerTypeRequest customerTypeRequest) {

        CustomerType customerType = customerTypeService.updateCustomerType(id,customerTypeRequest);
        return new Response<CustomerTypeDto>().buildSuccessResponse("CustomerType Updated Successfully",
                CustomerTypeDto.of(customerType), HttpStatus.OK);

    }

    @GetMapping
    public Response<Page<CustomerTypeDto>> getAllCustomerTypes(@PageableDefault Pageable pageable) {

        Page<CustomerType> customerTypes = customerTypeService.getAllCustomerTypes(pageable);
        return new Response<Page<CustomerTypeDto>>().buildSuccessResponse("SUCCESS",
                new PageImpl<>(CustomerTypeDto.of(customerTypes.getContent()),pageable, customerTypes.getTotalElements()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Response<CustomerTypeDto> getCustomerTypeById(@PathVariable Long id) {

        CustomerType customerType = customerTypeService.getCustomerTypeById(id);
        return new Response<CustomerTypeDto>().buildSuccessResponse("FOUND",
                CustomerTypeDto.of(customerType), HttpStatus.FOUND);

    }

    @GetMapping("/name")
    public Response<CustomerTypeDto> getCustomerTypeByName(@RequestParam String name) {
        CustomerType customerType = customerTypeService.getCustomerTypeByName(name);
        return new Response<CustomerTypeDto>().buildSuccessResponse("FOUND",
                CustomerTypeDto.of(customerType), HttpStatus.FOUND);
    }

    @GetMapping("/search")
    public Response<Page<CustomerTypeDto>> searchCustomerType(@RequestParam String searchParam, @PageableDefault Pageable pageable) {

        Page<CustomerType> customerTypes = customerTypeService.searchCustomerType(searchParam, pageable);
        return new Response<Page<CustomerTypeDto>>().buildSuccessResponse("SUCCESS",
                new PageImpl<>(CustomerTypeDto.of(customerTypes.getContent()),
                        pageable, customerTypes.getTotalElements()),HttpStatus.OK);

    }

    @PutMapping("/{id}/status")
    public Response<CustomerTypeDto> toggleCustomerTypeStatus(@PathVariable Long id) {

        CustomerType customerType = customerTypeService.toggleCustomerTypeStatus(id);
        return new Response<CustomerTypeDto>().buildSuccessResponse("Customer Type Status Updated Successfully",
                CustomerTypeDto.of(customerType), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<?> deleteCustomerType(@PathVariable Long id) {
        customerTypeService.deleteCustomerType(id);
        return new Response<>().buildSuccessResponse("Customer Type Deleted Successfully",HttpStatus.OK);
    }
}
