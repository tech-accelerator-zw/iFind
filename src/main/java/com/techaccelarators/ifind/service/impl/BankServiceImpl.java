package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.Bank;
import com.techaccelarators.ifind.dtos.bank.BankRequestDto;
import com.techaccelarators.ifind.service.BankService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BankServiceImpl implements BankService {
    @Override
    public Bank createBank(BankRequestDto bankRequestDto) {
        return null;
    }

    @Override
    public Bank updateBank(Long id, BankRequestDto bankRequestDto) {
        return null;
    }

    @Override
    public Page<Bank> getAllBanks(Pageable pageable) {
        return null;
    }

    @Override
    public Bank getBankById(Long id) {
        return null;
    }

    @Override
    public Bank getBankByName(String name) {
        return null;
    }

    @Override
    public Page<Bank> searchBank(String searchParam, Pageable pageable) {
        return null;
    }

    @Override
    public Bank toggleBankStatus(Long id) {
        return null;
    }

    @Override
    public void deleteBank(Long id) {

    }
}
