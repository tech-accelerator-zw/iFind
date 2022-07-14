package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.Customer;
import com.techaccelarators.ifind.domain.CustomerService;
import com.techaccelarators.ifind.domain.ServiceType;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.servicetype.ServiceTypeRequest;
import com.techaccelarators.ifind.dtos.servicetype.ServiceTypeResponseDto;
import com.techaccelarators.ifind.exception.RecordNotFoundException;
import com.techaccelarators.ifind.repository.CustomerServiceRepository;
import com.techaccelarators.ifind.repository.ServiceTypeRepository;
import com.techaccelarators.ifind.service.ServiceTypeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ServiceTypeImpl implements ServiceTypeService {
    private final ServiceTypeRepository serviceTypeRepository;
    private final CustomerServiceRepository customerServiceRepository;

    @Override
    public ServiceType createServiceType(ServiceTypeRequest serviceTypeRequest) {
        ServiceType serviceType = ServiceType.builder()
                .name(serviceTypeRequest.getName())
                .build();
        serviceType.setStatus(Status.ACTIVE);
        return serviceTypeRepository.save(serviceType);
    }

    @Override
    public Page<ServiceType> getAllServiceTypes(Pageable pageable) {
        return serviceTypeRepository.findAll(pageable);
    }

    @Override
    public ServiceTypeResponseDto getServiceTypeById(Long id) {
        ServiceType serviceType = serviceTypeRepository.findById(id)
                                    .orElseThrow(()-> new RecordNotFoundException("ServiceType Not Found"));
        List<CustomerService> customerServices = customerServiceRepository.findAllByServiceType(serviceType);
        Set<Customer> customers = customerServices.stream()
                .map(CustomerService::getCustomer)
                .collect(Collectors.toSet());
        return ServiceTypeResponseDto.of(serviceType,customers);
    }
    public ServiceType getServiceById(Long id){
        return serviceTypeRepository.findById(id)
                .orElseThrow(()-> new RecordNotFoundException("ServiceType Not Found"));
    }

    @Override
    public ServiceType getServiceTypeByName(String name) {
        return serviceTypeRepository.findServiceTypeByNameIgnoreCase(name)
                .orElseThrow(()-> new RecordNotFoundException("ServiceType not Found"));
    }

    @Override
    public ServiceType toggleServiceTypeStatus(Long id) {
        ServiceType serviceType = getServiceById(id);

        if (serviceType.getStatus() == Status.ACTIVE) {
            serviceType.setStatus(Status.INACTIVE);
        } else if (serviceType.getStatus() == Status.INACTIVE) {
            serviceType.setStatus(Status.ACTIVE);
        }
        return serviceTypeRepository.save(serviceType);
    }

    @Override
    public ServiceType updateServiceType(Long id,ServiceTypeRequest serviceTypeRequest) {
        ServiceType serviceType = getServiceById(id);
        serviceType.setName(serviceTypeRequest.getName());
        return serviceTypeRepository.save(serviceType);
    }
}
