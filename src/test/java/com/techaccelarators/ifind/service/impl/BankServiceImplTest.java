package com.techaccelarators.ifind.service.impl;

import com.techaccelarators.ifind.domain.Bank;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.bank.BankRequestDto;
import com.techaccelarators.ifind.exception.*;
import com.techaccelarators.ifind.repository.BankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankServiceImplTest {
    @Mock
    private BankRepository bankRepository;
    @Mock
    private Pageable pageable;
    @InjectMocks
    private BankServiceImpl bankService;
    private BankRequestDto bankRequestDto;
    private Bank bank;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.bankService = new BankServiceImpl(bankRepository);

        bankRequestDto = new BankRequestDto();
        bankRequestDto.setName("First Capital Bank");

        bank = Bank.builder().name("First Capital Bank").build();
        bank.setStatus(Status.ACTIVE);
    }

    @Test
    void createBankSuccess() {
        when(bankRepository.save(any(Bank.class))).thenReturn(bank);

        Bank createdBank = bankService.createBank(bankRequestDto);

        assertNotNull(createdBank.getId());
        assertEquals(bankRequestDto.getName(), createdBank.getName());
        assertEquals(Status.ACTIVE, createdBank.getStatus());
        verify(bankRepository, times(1)).save(any(Bank.class));
    }
    @Test
    public void createBankThrowsInvalidRequestException() {
        when(bankRepository.save(any())).thenThrow(InvalidRequestException.class);

        assertThrows(InvalidRequestException.class, () -> bankService.createBank(bankRequestDto));
        verify(bankRepository, times(1)).save(any());
    }

    @Test
    void updateBankSuccess() {
        when(bankRepository.findById(anyLong())).thenReturn(Optional.of(bank));
        when(bankRepository.save(any(Bank.class))).thenReturn(bank);

        BankRequestDto updatedBankRequestDto = new BankRequestDto();
        updatedBankRequestDto.setName("First Capital Bank");

        Bank updatedBank = bankService.updateBank(1L, updatedBankRequestDto);

        assertEquals(1L, updatedBank.getId());
        assertEquals(updatedBankRequestDto.getName(), updatedBank.getName());
        assertEquals(Status.ACTIVE, updatedBank.getStatus());

        verify(bankRepository, times(1)).findById(anyLong());
        verify(bankRepository, times(1)).save(any(Bank.class));
    }
    @Test
    void updateBankThrowsInvalidRequest() {
        when(bankRepository.findById(anyLong())).thenReturn(Optional.of(bank));
        when(bankRepository.save(any(Bank.class))).thenThrow(InvalidRequestException.class);

        assertThrows(InvalidRequestException.class,()-> bankService.updateBank(anyLong(),bankRequestDto));
        verify(bankRepository, times(1)).save(any(Bank.class));
    }

    @Test
    void getAllBanksSuccess() {
        Page<Bank> banksPage = Page.empty();
        when(bankRepository.findAll(any(Pageable.class))).thenReturn(banksPage);

        Page<Bank> retrievedBanksPage = bankService.getAllBanks(Pageable.unpaged());

        assertEquals(banksPage, retrievedBanksPage);
        verify(bankRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getBankByIdSuccess() {
        when(bankRepository.findById(anyLong())).thenReturn(Optional.of(bank));

        Bank retrievedBank = bankService.getBankById(anyLong());

        assertEquals(bank, retrievedBank);
        verify(bankRepository, times(1)).findById(anyLong());
    }
    @Test
    void getBankByIdThrowsRecordNotFoundException() {
        when(bankRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,()-> bankService.getBankById(anyLong()));
        verify(bankRepository, times(1)).findById(anyLong());
    }

    @Test
    void getBankByNameSuccess() {
        when(bankRepository.findBankByName(anyString())).thenReturn(Optional.of(bank));

        Bank retrievedBank = bankService.getBankByName("First Capital Bank");

        assertEquals(bank, retrievedBank);
        verify(bankRepository, times(1)).findBankByName(anyString());
    }
    @Test
    void getBankByNameThrowsRecordNotFoundException() {
        when(bankRepository.findBankByName(anyString())).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,()-> bankService.getBankByName(anyString()));
        verify(bankRepository, times(1)).findBankByName(anyString());
    }

    @Test
    void searchBankSuccess() {
        String searchParam = "bank";
        Page<Bank> expectedBankPage = new PageImpl<>(Collections.singletonList(bank));

        when(bankRepository.findAllByNameLikeIgnoreCase("%bank%", pageable)).thenReturn(expectedBankPage);

        Page<Bank> actualBankPage = bankService.searchBank(searchParam, pageable);

        assertEquals(expectedBankPage, actualBankPage);
        verify(bankRepository).findAllByNameLikeIgnoreCase("%bank%", pageable);
    }
    @Test
    void searchBankSuccessThrowsInvalidRequestException() {
        String searchParam = "invalid-bank";

        when(bankRepository.findAllByNameLikeIgnoreCase("%invalid-bank%", pageable))
                .thenThrow(new InvalidRequestException("Invalid bank"));

        assertThrows(InvalidRequestException.class, () -> bankService.searchBank(searchParam, pageable));
        verify(bankRepository).findAllByNameLikeIgnoreCase("%invalid-bank%", pageable);
    }

    @Test
    void toggleBankStatusSuccess() {
        when(bankRepository.findById(anyLong())).thenReturn(Optional.of(bank));
        when(bankRepository.save(any(Bank.class))).thenReturn(bank);

        Bank resultBank = bankService.toggleBankStatus(anyLong());

        verify(bankRepository, times(1)).findById(anyLong());
        verify(bankRepository, times(1)).save(any(Bank.class));
        assertEquals(Status.INACTIVE, resultBank.getStatus());

        resultBank = bankService.toggleBankStatus(anyLong());

        verify(bankRepository, times(2)).findById(anyLong());
        verify(bankRepository, times(2)).save(any(Bank.class));
        assertEquals(Status.ACTIVE, resultBank.getStatus());
    }
    @Test
    void toggleBankStatusWithInvalidBankId() {

        when(bankRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> bankService.toggleBankStatus(1L));
        verify(bankRepository, times(1)).findById(anyLong());
        verify(bankRepository, never()).save(any(Bank.class));
    }

    @Test
    void deleteBankSuccess() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "admin",
                List.of(new SimpleGrantedAuthority("ADMIN")));
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(bankRepository.findById(anyLong())).thenReturn(Optional.of(bank));

        bankService.deleteBank(1L);

        verify(bankRepository, times(1)).delete(bank);
    }

    @Test
    public void deleteBankUnauthorized() {
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(UnauthorizedException.class, () -> bankService.deleteBank(1L));
        verify(bankRepository,never()).delete(bank);
    }

    @Test
    public void deleteBankForbidden() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "user",
                List.of(new SimpleGrantedAuthority("USER")));
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(ForbiddenException.class, () -> bankService.deleteBank(1L));
        verify(bankRepository,never()).delete(bank);
    }

    @Test
    public void deleteBankInternalServerError() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "admin",
                List.of(new SimpleGrantedAuthority("ADMIN")));
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(bankRepository.findById(anyLong())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> bankService.deleteBank(1L));
        verify(bankRepository,never()).delete(bank);
    }

}