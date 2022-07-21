package com.techaccelarators.ifind.domain;

import com.techaccelarators.ifind.domain.util.Address;
import com.techaccelarators.ifind.domain.util.ContactDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "customer")
public class Customer extends BaseEntity {
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;
    @Embedded
    private Address address;

    @OneToOne
    private CustomerType customerType;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    private Bank bank;
    @Column(name = "account_number", unique = true, nullable = false)
    private Long accountNumber;

    @Embedded
    private ContactDetails contactDetails;
    @ManyToOne
    private ServiceType serviceType;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<CustomerBranch> customerBranches;
}
