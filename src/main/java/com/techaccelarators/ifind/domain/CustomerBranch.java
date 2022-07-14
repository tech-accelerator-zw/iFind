package com.techaccelarators.ifind.domain;

import com.techaccelarators.ifind.domain.util.Address;
import com.techaccelarators.ifind.domain.util.ContactDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer_branch")
public class CustomerBranch extends BaseEntity{
    @Column(name = "name")
    private String name;
    @Embedded
    @Column(name = "address")
    private Address address;
    @Embedded
    @Column(name = "contact_details")
    private ContactDetails contactDetails;

}
