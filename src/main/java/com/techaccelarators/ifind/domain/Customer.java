package com.techaccelarators.ifind.domain;

import com.techaccelarators.ifind.common.jpa.DefaultIdentifierAuditedEntity;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.domain.util.Address;
import com.techaccelarators.ifind.domain.util.ContactDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "customer",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name"),
                @UniqueConstraint(columnNames = "uuid"),
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer extends DefaultIdentifierAuditedEntity {
    @Column(name = "name", nullable = false)
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
    private String accountNumber;

    @Embedded
    private ContactDetails contactDetails;
    @ManyToOne
    private ServiceType serviceType;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<CustomerBranch> customerBranches;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
