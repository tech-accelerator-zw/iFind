package com.techaccelarators.ifind.dtos.bank;

import com.techaccelarators.ifind.domain.Bank;
import com.techaccelarators.ifind.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankDto {
    private Long id;

    private String name;

    private Status status;

    public static BankDto of(Bank bank) {
        if (Objects.isNull(bank)) {
            return null;
        }
        return new BankDto(bank.getId(), bank.getName(),bank.getStatus());
    }

    public static List<BankDto> of(List<Bank> banks) {
        if(Objects.isNull(banks)){
            return Collections.emptyList();
        }
        return banks.stream().map(BankDto::of).collect(Collectors.toList());
    }
}
