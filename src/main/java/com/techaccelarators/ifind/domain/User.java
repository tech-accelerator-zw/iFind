package com.techaccelarators.ifind.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class User extends BaseEntity{
    @Column
    private String name;
    @Column(name = "user_name",unique = true)
    private String username;
    @Column(name = "email",unique = true)
    private String email;
    @Column
    private String password;
}
