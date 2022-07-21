package com.techaccelarators.ifind.domain.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BankingDetails {

    @Column(name = "bank_name",nullable = false)
    private String bankName;

    @Column(name = "account_number", unique = true, nullable = false)
    private Long accountNumber;
}
