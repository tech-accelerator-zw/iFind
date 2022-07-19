package com.techaccelarators.ifind.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_roles")
public class UserRole extends BaseEntity{
    @ManyToOne(cascade = CascadeType.ALL)
    private UserAccount userAccount;
    @ManyToOne(cascade = CascadeType.ALL)
    private Role role;
}
