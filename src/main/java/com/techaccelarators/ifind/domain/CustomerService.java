package com.techaccelarators.ifind.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer_service")
public class CustomerService extends BaseEntity {
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private ServiceType serviceType;
}
