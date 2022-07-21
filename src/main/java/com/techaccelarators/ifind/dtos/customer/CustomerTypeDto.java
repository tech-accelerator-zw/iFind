package com.techaccelarators.ifind.dtos.customer;

import com.techaccelarators.ifind.domain.CustomerType;
import com.techaccelarators.ifind.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTypeDto {
    private Long id;

    private String name;

    private Status status;

    public static CustomerTypeDto of(CustomerType customerType) {
        if (Objects.isNull(customerType)) {
            return null;
        }
        return new CustomerTypeDto(customerType.getId(), customerType.getName(),customerType.getStatus());
    }

    public static List<CustomerTypeDto> of(List<CustomerType> customerTypes) {
        if(Objects.isNull(customerTypes)){
            return Collections.emptyList();
        }
        return customerTypes.stream().map(CustomerTypeDto::of).collect(Collectors.toList());
    }
}
