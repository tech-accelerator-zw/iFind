package com.techaccelarators.ifind.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_account")
public class UserAccount extends BaseEntity{
    @Column(name= "name")
    private String name;
    @Column(name = "user_name",unique = true)
    private String username;
    @Column(name = "email",unique = true)
    private String email;
    @Column(name = "password")
    private String password;
}
