package com.techaccelarators.ifind.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bank")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bank extends BaseEntity{
    @Column(name = "name", unique = true, nullable = false)
    private String name;
}
