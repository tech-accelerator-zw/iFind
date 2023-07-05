package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.Bank;
import com.techaccelarators.ifind.domain.ServiceType;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.bank.BankRequestDto;
import com.techaccelarators.ifind.exception.*;
import com.techaccelarators.ifind.repository.BankRepository;
import com.techaccelarators.ifind.service.BankService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;
    @Override
    public Bank createBank(BankRequestDto bankRequestDto) {
        log.info("Creating a Bank with arguments: {}",bankRequestDto);
        checkUnique(bankRequestDto, null);
        Bank bank = Bank.builder()
                .name(bankRequestDto.getName())
                .build();
        bank.setStatus(Status.ACTIVE);
        return bankRepository.save(bank);
    }
    @Override
    public Bank updateBank(Long id, BankRequestDto bankRequestDto) {
        log.info("Updating a bank with an id: {}",id);
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
        log.info("Getting all available banks");
        return bankRepository.findAll(pageable);
    }
    @Override
    public Bank getBankById(Long id) {
        log.info("Getting a bank with id: {}",id);
        return bankRepository.findById(id)
                .orElseThrow(()-> new RecordNotFoundException("Bank Not Found"));
    }
    @Override
    public Bank getBankByName(String name) {
        log.info("Getting a bank with name: {}",name);
        return bankRepository.findBankByName(name)
                .orElseThrow(()->{
                    log.info("Bank with name: {}  could not be found ",name);
                    return new RecordNotFoundException("Bank Not Found");
                });
    }
    @Override
    public Page<Bank> searchBank(String searchParam, Pageable pageable) {
        try {
            String searchWord = "%".concat(searchParam).concat("%");
            return bankRepository.findAllByNameLikeIgnoreCase(searchWord, pageable);
        } catch (InvalidRequestException ex) {
            log.info("Could not find the searched bank with name: {}",searchParam);
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException("Unauthorized");
        }
        String role = authentication.getAuthorities().stream().findFirst().get().getAuthority();
        if (!role.equals("ADMIN")) {
            throw new ForbiddenException("Forbidden");
        }
        try {
            Bank bank = getBankById(id);
            bankRepository.delete(bank);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete the service type");
        }
    }
    private void checkUnique(BankRequestDto request, Long id) {
        log.info("Checking bank uniqueness with name: {} and id: {}", request.getName(), id);
        bankRepository.findByNameIgnoreCase(request.getName())
                .filter(bank -> !bank.getId().equals(id))
                .ifPresent(bank -> {
                    throw new InvalidRequestException("Bank with given name already exists");
                });
    }
}
