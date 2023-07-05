package com.techaccelarators.ifind.controller;

import com.techaccelarators.ifind.domain.Bank;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.bank.BankDto;
import com.techaccelarators.ifind.dtos.bank.BankRequestDto;
import com.techaccelarators.ifind.exception.*;
import com.techaccelarators.ifind.service.BankService;
import com.techaccelarators.ifind.util.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BankControllerTest {
    @Mock
    private BankService bankService;
    @Mock
    private Pageable pageable;
    @InjectMocks
    private BankController bankController;

    Bank RECORD_1 = new Bank("Ecobank");
    Bank RECORD_2 = new Bank("CABS");
    Bank RECORD_3 = new Bank("Steward Bank");

    @BeforeAll
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void injectMocks(){
        this.bankController = new BankController(bankService);
    }

    @Test
    @DisplayName("createBankSuccess")
    void createBank() {
        //Arrange
        BankRequestDto bankRequestDto = new BankRequestDto("First Capital");
        Bank bank = new Bank();
        bank.setId(4L);
        bank.setName(bankRequestDto.getName());
        when(bankService.createBank(any(BankRequestDto.class))).thenReturn(bank);

        //Act
        Response<BankDto> response = bankController.createBank(bankRequestDto);

        //Assert
        assertEquals("Bank Created Successfully", response.getMessage());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        BankDto bankDto = response.getResult();
        assertNotNull(bankDto);
        assertEquals(4L, bankDto.getId());
        assertEquals("First Capital", bankDto.getName());
    }

    @Test
    @DisplayName("updateBankSuccess")
    void updateBank() {
        // Arrange
        BankRequestDto bankRequestDto = new BankRequestDto();
        Bank bank = new Bank();
        bank.setId(1L);
        Mockito.when(bankService.updateBank(bank.getId(), bankRequestDto)).thenReturn(bank);

        // Act
        Response<BankDto> response = bankController.updateBank(bank.getId(), bankRequestDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Bank Updated Successfully", response.getMessage());
        assertNotNull(response.getResult());
        assertEquals(1L, response.getResult().getId());
    }

    @Test
    @DisplayName("getAllBanksSuccess")
    void getAllBanks() {

        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Bank> banks = new PageImpl<>(Arrays.asList(RECORD_1,RECORD_2,RECORD_3), pageable, 3L);
        when(bankService.getAllBanks(pageable)).thenReturn(banks);

        // Act
        Response<Page<BankDto>> response = bankController.getAllBanks(pageable);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(3, response.getResult().getTotalElements());
        assertEquals(3, response.getResult().getContent().size());
    }
    @Test
    @DisplayName("getAllBanksReturnsEmptyResults")
    public void testGetAllBanksWithEmptyResult() {
        // arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Bank> page = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(bankService.getAllBanks(pageable)).thenReturn(page);
        // act
        Response<Page<BankDto>> response = bankController.getAllBanks(pageable);
        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(0, response.getResult().getTotalElements());
        assertEquals(Collections.emptyList(), response.getResult().getContent());
    }

    @Test
    @DisplayName("getBankByIdSuccess")
    void getBankById() {
        // Arrange
        RECORD_1.setId(1L);
        when(bankService.getBankById(RECORD_1.getId())).thenReturn(RECORD_1);
        // Act
        Response<BankDto> response = bankController.getBankById(1L);
        // Assert
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals("FOUND", response.getMessage());
        assertEquals("Ecobank",response.getResult().getName());


    }
    @Test
    @DisplayName("getBankByIdThrowsRecordNotFound")
    void testGetBankByIdNotFound() {
        // Arrange
        Long id = 1L;
        when(bankService.getBankById(id)).thenThrow(new RecordNotFoundException("Bank not found"));
        assertThrows(RecordNotFoundException.class, () -> bankController.getBankById(id));
    }
    @Test
    @DisplayName("getBankByIdThrowsInternalServerError")
    public void testGetBankByIdInternalServerError() {
        // Arrange
        Long id = 6L;
        when(bankService.getBankById(id)).thenThrow(new RuntimeException("Something went wrong"));
        assertThrows(RuntimeException.class, () -> bankController.getBankById(id));
    }

    @Test
    @DisplayName("getBankByNameSuccess")
    void getBankByName() {
        // Arrange
        when(bankService.getBankByName("CABS")).thenReturn(RECORD_2);
        // Act
        Response<BankDto> response = bankController.getBankByName("CABS");
        // Assert
        assertEquals(HttpStatus.FOUND,response.getStatusCode());
        assertEquals("FOUND",response.getMessage());
        assertEquals("CABS",response.getResult().getName());
    }
    @Test
    @DisplayName("getBankByIdThrowsRecordNotFound")
    public void testGetBankByNameNotFound() throws Exception {
        // Arrange
        String bankName = "Non-existent Bank";
        when(bankService.getBankByName(bankName)).thenThrow(new RecordNotFoundException("Bank not found"));
        // Act and Assert
        assertThrows(RecordNotFoundException.class, () -> bankController.getBankByName(bankName));
    }
    @Test
    @DisplayName("getBankByNameThrowsInternalServerError")
    public void testGetBankByNameInternalServerError() throws Exception {
        // Arrange
        String bankName = "My Bank";
        when(bankService.getBankByName(bankName)).thenThrow(new RuntimeException("Something went wrong"));
        // Act
        assertThrows(RuntimeException.class,()-> bankController.getBankByName(bankName));
    }

    @Test
    @DisplayName("searchBankSuccess")
    void searchBank() {
        // Arrange
        String searchParam = "Steward Bank";
        Page<Bank> banks = new PageImpl<>(Collections.singletonList(RECORD_3),pageable,1l);
        when(bankService.searchBank(searchParam, pageable)).thenReturn(banks);
        // Act
        Response<Page<BankDto>> response = bankController.searchBank(searchParam, pageable);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(1, response.getResult().getContent().size());
        assertEquals("Steward Bank", response.getResult().getContent().get(0).getName());

    }
    @Test
    @DisplayName("searchBankThrowsInvalidRequestException")
    void searchBankThrowsInvalidRequestException() {
        // Arrange
        String searchParam = "Steward Bank";
        when(bankService.searchBank(searchParam, pageable)).thenThrow(new InvalidRequestException("Invalid Request"));
        // Act
        assertThrows(InvalidRequestException.class,()-> bankController.searchBank(searchParam,pageable));

    }
    @Test
    @DisplayName("searchBankThrowsInternalServerError")
    void searchBankThrowsInternalServerError() {
        // Arrange
        String searchParam = "Steward Bank";
        when(bankService.searchBank(searchParam, pageable)).thenThrow(new RuntimeException("Something went wrong"));
        // Act
        assertThrows(RuntimeException.class,()-> bankController.searchBank(searchParam,pageable));

    }

    @Test
    @DisplayName("toggleBankStatusSuccess")
    void toggleBankStatus() {
        // Arrange
        RECORD_1.setId(1L);
        RECORD_1.setStatus(Status.ACTIVE);
        when(bankService.toggleBankStatus(1l)).thenReturn(RECORD_1);
        // Act
        Response<BankDto> response = bankController.toggleBankStatus(RECORD_1.getId());
        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Bank Status Updated Successfully", response.getMessage());
        assertNotNull(response.getResult());
        assertEquals(RECORD_1.getId(), response.getResult().getId());
        assertEquals(RECORD_1.getStatus(), response.getResult().getStatus());
    }

    @Test
    @DisplayName("deleteBankSuccessfully")
    public void testDeleteBank_success() {
        // Arrange
        RECORD_1.setId(1L);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Act
        Response<?> response = bankController.deleteBank(RECORD_1.getId());
        // Assert
        verify(bankService).deleteBank(RECORD_1.getId());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Bank Deleted Successfully", response.getMessage());

    }

    @Test
    @DisplayName("deleteBankThrowsUnauthorized")
    public void testDeleteBank_unauthorized() {
        //Arrange
        Long id = 1L;
        SecurityContextHolder.getContext().setAuthentication(null);

        doThrow(UnauthorizedException.class).when(bankService).deleteBank(id);

        assertThrows(UnauthorizedException.class,()-> bankController.deleteBank(id));
        verify(bankService).deleteBank(id);
    }

    @Test
    @DisplayName("deleteBankForbidden")
    public void testDeleteBank_forbidden() {
        //Arrange
        Long id = 1L;
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //Act
        doThrow(ForbiddenException.class).when(bankService).deleteBank(id);
        //Assert
        assertThrows(ForbiddenException.class,()-> bankController.deleteBank(id));
        verify(bankService).deleteBank(id);
    }


}