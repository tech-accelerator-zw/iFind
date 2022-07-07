package com.techaccelarators.ifind.domain;

import com.techaccelarators.ifind.domain.util.Address;
import com.techaccelarators.ifind.domain.util.BankingDetails;
import com.techaccelarators.ifind.domain.util.ContactDetails;
import com.techaccelarators.ifind.domain.util.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "customer")
public class Customer extends BaseEntity {
    @Column(name = "name",
            unique = true,
            nullable = false)
    private String name;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    @Column(name = "image_url")
    private String imageUrl;

    @Embedded
    private BankingDetails bankingDetails;

    @Embedded
    private ContactDetails contactDetails;
    @OneToOne
    private ServiceType serviceType;
}
