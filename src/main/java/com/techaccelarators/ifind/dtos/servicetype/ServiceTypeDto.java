package com.techaccelarators.ifind.dtos.servicetype;

import com.techaccelarators.ifind.domain.ServiceType;
import com.techaccelarators.ifind.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceTypeDto {
    private Long id;
    private String name;
    private String icon;
    private Status status;

    public static ServiceTypeDto of(ServiceType serviceType){
        if(Objects.isNull(serviceType)){
            return null;
        }
        return new ServiceTypeDto(serviceType.getId(), serviceType.getName(), serviceType.getImageUrl(), serviceType.getStatus());
    }

    public static List<ServiceTypeDto> of(Collection<ServiceType> serviceTypes){
        if(Objects.isNull(serviceTypes)){
            return Collections.emptyList();
        }
        return serviceTypes.stream().map(ServiceTypeDto::of).collect(Collectors.toList());
    }
}
