package com.techaccelarators.ifind.dtos.servicetype;

import com.techaccelarators.ifind.domain.Customer;
import com.techaccelarators.ifind.domain.ServiceType;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.customer.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceTypeResponseDto {
    private Long id;
    private String name;
    private Status status;
    private Set<CustomerDto> customerSet;

    public static ServiceTypeResponseDto of(ServiceType serviceType, Set<Customer> customers){
        if(Objects.isNull(serviceType)){
            return null;
        }
        return new ServiceTypeResponseDto(serviceType.getId(), serviceType.getName(),serviceType.getStatus(),
                customers.stream().map(CustomerDto::of).collect(Collectors.toSet()));
    }
}
