package com.techaccelarators.ifind.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "roles")
public class Role extends BaseEntity{
    @Column(length = 40)
    private String name;
}
