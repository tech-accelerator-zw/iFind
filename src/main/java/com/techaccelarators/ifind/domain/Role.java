package com.techaccelarators.ifind.domain;

import com.techaccelarators.ifind.common.jpa.DefaultIdentifierAuditedEntity;
import com.techaccelarators.ifind.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "roles",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name"),
                @UniqueConstraint(columnNames = "uuid"),
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role extends DefaultIdentifierAuditedEntity {
    @Column(name="name",length = 40)
    private String name;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
