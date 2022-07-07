package com.techaccelarators.ifind.domain.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Email;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactDetails {

    @Column(name = "telephone_number")
    private String telephone;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Email
    @Column(name = "contact_email")
    private String email;
}
