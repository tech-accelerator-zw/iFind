package com.techaccelarators.ifind.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_account")
public class UserAccount extends BaseEntity{
    @Column(name = "user_name",unique = true)
    private String username;

    @Column(name = "email",unique = true)
    private String email;

    @Column(name = "first_name", length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "is_otp_required")
    private Boolean isOtpRequired;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "last_password_reset_date")
    private Date lastPasswordResetDate;
}
