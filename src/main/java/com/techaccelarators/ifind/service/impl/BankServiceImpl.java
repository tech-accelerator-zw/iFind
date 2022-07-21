package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.Bank;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.bank.BankRequestDto;
import com.techaccelarators.ifind.exception.InvalidRequestException;
import com.techaccelarators.ifind.exception.RecordNotFoundException;
import com.techaccelarators.ifind.repository.BankRepository;
import com.techaccelarators.ifind.service.BankService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;
    @Override
    public Bank createBank(BankRequestDto bankRequestDto) {
        checkUnique(bankRequestDto, null);
        Bank bank = Bank.builder()
                .name(bankRequestDto.getName())
                .build();
        bank.setStatus(Status.ACTIVE);
        return bankRepository.save(bank);
    }

    @Override
    public Bank updateBank(Long id, BankRequestDto bankRequestDto) {
        Bank bank = getBankById(id);
        checkUnique(bankRequestDto, id);
        bank.setName(bankRequestDto.getName());
        if(bankRepository.existsByName(bankRequestDto.getName())){
            throw new InvalidRequestException("Bank Name Already In Use");
        }

        return bankRepository.save(bank);
    }

    @Override
    public Page<Bank> getAllBanks(Pageable pageable) {
        return bankRepository.findAll(pageable);
    }

    @Override
    public Bank getBankById(Long id) {
        return bankRepository.findById(id)
                .orElseThrow(()-> new RecordNotFoundException("Bank Not Found"));
    }

    @Override
    public Bank getBankByName(String name) {
        return bankRepository.findBankByName(name)
                .orElseThrow(()-> new RecordNotFoundException("Bank Not Found"));
    }

    @Override
    public Page<Bank> searchBank(String searchParam, Pageable pageable) {
        try {
            String searchWord = "%".concat(searchParam).concat("%");
            return bankRepository.findAllByNameLikeIgnoreCase(searchWord, pageable);
        } catch (Exception ex) {
            throw new InvalidRequestException(ex.getMessage());
        }
    }

    @Override
    public Bank toggleBankStatus(Long id) {
        Bank bank = getBankById(id);

        if (bank.getStatus() == Status.ACTIVE) {
            bank.setStatus(Status.INACTIVE);
        } else if (bank.getStatus() == Status.INACTIVE) {
            bank.setStatus(Status.ACTIVE);
        }
        return bankRepository.save(bank);
    }

    @Override
    public void deleteBank(Long id) {
        Bank bank = getBankById(id);
        bankRepository.delete(bank);
    }
    private void checkUnique(BankRequestDto request, Long id) {
        log.info("Checking bank uniqueness with name: {} and id: {}", request.getName(), id);
        bankRepository.findByNameIgnoreCase(request.getName())
                .filter(customer -> !customer.getId().equals(id))
                .ifPresent(customer -> {
                    throw new InvalidRequestException("Bank with given name already exists");
                });
    }
}
