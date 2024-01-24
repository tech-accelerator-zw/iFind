package com.techaccelarators.ifind.domain;

import com.techaccelarators.ifind.common.jpa.DefaultIdentifierAuditedEntity;
import com.techaccelarators.ifind.domain.enums.Status;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_account",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "uuid")
        }
)
@Getter
@Setter
public class UserRole extends DefaultIdentifierAuditedEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    private UserAccount userAccount;
    @ManyToOne(cascade = CascadeType.ALL)
    private Role role;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
