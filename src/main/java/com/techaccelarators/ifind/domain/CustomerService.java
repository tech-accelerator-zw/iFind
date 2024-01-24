package com.techaccelarators.ifind.domain;

import com.techaccelarators.ifind.common.jpa.DefaultIdentifierAuditedEntity;
import com.techaccelarators.ifind.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "customer_service",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "uuid")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerService extends DefaultIdentifierAuditedEntity {
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private ServiceType serviceType;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
