package com.techaccelarators.ifind.controller;

import com.techaccelarators.ifind.domain.Customer;
import com.techaccelarators.ifind.domain.ServiceType;
import com.techaccelarators.ifind.dtos.customer.CustomerDto;
import com.techaccelarators.ifind.dtos.servicetype.ServiceTypeDto;
import com.techaccelarators.ifind.dtos.servicetype.ServiceTypeRequest;
import com.techaccelarators.ifind.dtos.servicetype.ServiceTypeResponseDto;
import com.techaccelarators.ifind.service.ServiceTypeService;
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
@RequestMapping("/v1/service-type")
public class ServiceTypeController {
    private final ServiceTypeService serviceTypeService;

    @PostMapping
    public Response<ServiceTypeDto> createServiceType(@Valid @RequestBody ServiceTypeRequest serviceTypeRequest) {

        ServiceType serviceType = serviceTypeService.createServiceType(serviceTypeRequest);
        return new Response<ServiceTypeDto>().buildSuccessResponse("Service Type Created Successfully",
                ServiceTypeDto.of(serviceType), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Response<ServiceTypeDto> updateServiceType(@PathVariable Long id, @Valid @RequestBody ServiceTypeRequest serviceTypeRequest) {

        ServiceType serviceType = serviceTypeService.updateServiceType(id,serviceTypeRequest);
        return new Response<ServiceTypeDto>().buildSuccessResponse("Service Type Updated Successfully",
                ServiceTypeDto.of(serviceType), HttpStatus.OK);
    }

    @GetMapping
    public Response<Page<ServiceTypeDto>> getAllServiceTypes(@PageableDefault Pageable pageable) {

        Page<ServiceType> serviceTypes = serviceTypeService.getAllServiceTypes(pageable);
        return new Response<Page<ServiceTypeDto>>().buildSuccessResponse("SUCCESS",
                new PageImpl<>(ServiceTypeDto.of(serviceTypes.getContent()),pageable, serviceTypes.getTotalElements()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Response<ServiceTypeResponseDto> getServiceTypeById(@PathVariable Long id) {

        return new Response<ServiceTypeResponseDto>().buildSuccessResponse("FOUND",
                serviceTypeService.getServiceTypeById(id), HttpStatus.FOUND);
    }

    @GetMapping("/name")
    public Response<ServiceTypeDto> getServiceTypeByName(@RequestParam String name) {

        ServiceType serviceType = serviceTypeService.getServiceTypeByName(name);
        return new Response<ServiceTypeDto>().buildSuccessResponse("FOUND",
                ServiceTypeDto.of(serviceType), HttpStatus.FOUND);
    }

    @GetMapping("/search")
    public Response<Page<ServiceTypeDto>> searchCustomer(@RequestParam String searchParam, @PageableDefault Pageable pageable) {

        Page<ServiceType> serviceTypes = serviceTypeService.searchServiceType(searchParam, pageable);
        return new Response<Page<ServiceTypeDto>>().buildSuccessResponse("SUCCESSFUL",
                new PageImpl<>(ServiceTypeDto.of(serviceTypes.getContent()),
                        pageable, serviceTypes.getTotalElements()),HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public Response<ServiceTypeDto> toggleServiceTypeStatus(@PathVariable Long id) {

        ServiceType serviceType = serviceTypeService.toggleServiceTypeStatus(id);
        return new Response<ServiceTypeDto>().buildSuccessResponse("ServiceType Status Updated Successfully",
                ServiceTypeDto.of(serviceType), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<?> deleteCustomer(@PathVariable Long id) {

        serviceTypeService.deleteServiceType(id);
        return new Response<>().buildSuccessResponse("ServiceType Deleted Successfully",HttpStatus.OK);
    }
}
