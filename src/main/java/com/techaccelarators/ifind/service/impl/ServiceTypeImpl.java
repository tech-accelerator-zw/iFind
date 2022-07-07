package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.ServiceType;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.servicetype.ServiceTypeRequest;
import com.techaccelarators.ifind.exception.RecordNotFoundException;
import com.techaccelarators.ifind.repository.ServiceTypeRepository;
import com.techaccelarators.ifind.service.ServiceTypeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ServiceTypeImpl implements ServiceTypeService {
    private final ServiceTypeRepository serviceTypeRepository;

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
    public ServiceType getServiceTypeById(Long id) {
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
        ServiceType serviceType = getServiceTypeById(id);

        if (serviceType.getStatus() == Status.ACTIVE) {
            serviceType.setStatus(Status.INACTIVE);
        } else if (serviceType.getStatus() == Status.INACTIVE) {
            serviceType.setStatus(Status.ACTIVE);
        }
        return serviceTypeRepository.save(serviceType);
    }

    @Override
    public ServiceType updateServiceType(Long id,ServiceTypeRequest serviceTypeRequest) {
        ServiceType serviceType = getServiceTypeById(id);
        serviceType.setName(serviceTypeRequest.getName());
        return serviceTypeRepository.save(serviceType);
    }
}
