package com.techaccelarators.ifind.domain;

import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.domain.util.Audit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @org.springframework.data.annotation.Version
    private Long version;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Embedded
    private Audit audit = new Audit();

}
