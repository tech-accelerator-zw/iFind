package com.techaccelarators.ifind.dtos.branch;

import com.techaccelarators.ifind.domain.CustomerBranch;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.domain.util.Address;
import com.techaccelarators.ifind.domain.util.ContactDetails;
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
public class CustomerBranchDto {
    private Long id;

    private String name;

    private Address address;

    private ContactDetails contactDetails;

    private Status status;

    public static CustomerBranchDto of(CustomerBranch customerBranch) {
        if (Objects.isNull(customerBranch)) {
            return null;
        }
        return new CustomerBranchDto(customerBranch.getId(), customerBranch.getName(),customerBranch.getAddress(),
                customerBranch.getContactDetails(),customerBranch.getStatus());
    }

    public static List<CustomerBranchDto> of(List<CustomerBranch> customerBranches) {
        if(Objects.isNull(customerBranches)){
            return Collections.emptyList();
        }
        return customerBranches.stream().map(CustomerBranchDto::of).collect(Collectors.toList());
    }
}
