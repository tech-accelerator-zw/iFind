package com.techaccelarators.ifind.service;

import com.techaccelarators.ifind.domain.Bank;
import com.techaccelarators.ifind.dtos.bank.BankRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BankService {
    Bank createBank(BankRequestDto bankRequestDto);

    Bank updateBank(Long id, BankRequestDto bankRequestDto);

    Page<Bank> getAllBanks(Pageable pageable);

    Bank getBankById(Long id);

    Bank getBankByName(String name);

    Page<Bank> searchBank(String searchParam, Pageable pageable);

    Bank toggleBankStatus(Long id);

    void deleteBank(Long id);
}
