package com.techaccelarators.ifind.domain;

import com.techaccelarators.ifind.commons.jpa.DefaultIdentifierAuditedEntity;
import com.techaccelarators.ifind.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "user_account",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "user_name"),
                @UniqueConstraint(columnNames = "uuid"),
                @UniqueConstraint(columnNames = "email"),
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount extends DefaultIdentifierAuditedEntity {
    @Column(name = "user_name")
    private String username;

    @Column(name = "email", nullable = false)
    @Email(message = "Please provide a valid e-mail")
    private String email;

    @Column(name = "first_name", length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    @NotEmpty(message = "Please provide your first name")
    private String firstName;

    @Column(name = "last_name", length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    @NotEmpty(message = "Please provide your last name")
    private String lastName;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "is_otp_required")
    private Boolean isOtpRequired;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "last_password_reset_date")
    private Date lastPasswordResetDate;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
