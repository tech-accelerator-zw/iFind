package com.techaccelarators.ifind.service;

import com.techaccelarators.ifind.domain.ServiceType;
import com.techaccelarators.ifind.dtos.servicetype.ServiceTypeRequest;
import com.techaccelarators.ifind.dtos.servicetype.ServiceTypeResponseDto;
import com.techaccelarators.ifind.repository.ServiceTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServiceTypeService {
    ServiceType createServiceType(ServiceTypeRequest serviceTypeRequest);

    Page<ServiceType> getAllServiceTypes(Pageable pageable);

    ServiceTypeResponseDto getServiceTypeById(Long id);

    ServiceType getServiceTypeByName(String name);

    ServiceType toggleServiceTypeStatus(Long id);

    ServiceType updateServiceType(Long id,ServiceTypeRequest serviceTypeRequest);

    Page<ServiceType> searchServiceType(String searchParam, Pageable pageable);
}
