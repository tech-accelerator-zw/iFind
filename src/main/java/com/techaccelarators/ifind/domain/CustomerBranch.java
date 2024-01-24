package com.techaccelarators.ifind.domain;

import com.techaccelarators.ifind.commons.jpa.DefaultIdentifierAuditedEntity;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.domain.util.Address;
import com.techaccelarators.ifind.domain.util.ContactDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "customer_branch",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "uuid")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerBranch extends DefaultIdentifierAuditedEntity {
    @Column(name = "name")
    private String name;
    @Embedded
    @Column(name = "address")
    private Address address;
    @Embedded
    @Column(name = "contact_details")
    private ContactDetails contactDetails;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

}
