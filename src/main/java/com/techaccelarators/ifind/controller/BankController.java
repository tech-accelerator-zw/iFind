package com.techaccelarators.ifind.controller;

import com.techaccelarators.ifind.domain.Bank;
import com.techaccelarators.ifind.dtos.bank.BankDto;
import com.techaccelarators.ifind.dtos.bank.BankRequestDto;
import com.techaccelarators.ifind.exception.*;
import com.techaccelarators.ifind.service.BankService;
import com.techaccelarators.ifind.util.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/v1/bank")
@Tag(name = "Bank Controller", description = "Rest Controller for Banks")
public class BankController {
    private final BankService bankService;
    @PostMapping
    public Response<BankDto> createBank(@Valid @RequestBody BankRequestDto bankRequestDto) {

        Bank bank = bankService.createBank(bankRequestDto);
        return new Response<BankDto>().buildSuccessResponse("Bank Created Successfully",
                BankDto.of(bank), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Response<BankDto> updateBank(@PathVariable Long id, @Valid @RequestBody BankRequestDto bankRequestDto) {

        Bank bank = bankService.updateBank(id,bankRequestDto);
        return new Response<BankDto>().buildSuccessResponse("Bank Updated Successfully",
                BankDto.of(bank), HttpStatus.OK);
    }

    @GetMapping
    public Response<Page<BankDto>> getAllBanks(@PageableDefault Pageable pageable) {

        Page<Bank> banks = bankService.getAllBanks(pageable);
        return new Response<Page<BankDto>>().buildSuccessResponse("SUCCESS",
                new PageImpl<>(BankDto.of(banks.getContent()),pageable, banks.getTotalElements()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Response<BankDto> getBankById(@PathVariable Long id) {

        Bank bank = bankService.getBankById(id);
        return new Response<BankDto>().buildSuccessResponse("FOUND",
                BankDto.of(bank), HttpStatus.FOUND);
    }

    @GetMapping("/name")
    public Response<BankDto> getBankByName(@RequestParam String name) {

        Bank bank = bankService.getBankByName(name);
        return new Response<BankDto>().buildSuccessResponse("FOUND",
                BankDto.of(bank), HttpStatus.FOUND);
    }
    @GetMapping("/search")
    public Response<Page<BankDto>> searchBank(@RequestParam String searchParam, @PageableDefault Pageable pageable) {

        Page<Bank> banks = bankService.searchBank(searchParam, pageable);
        return new Response<Page<BankDto>>().buildSuccessResponse("SUCCESS",
                new PageImpl<>(BankDto.of(banks.getContent()),
                        pageable, banks.getTotalElements()),HttpStatus.OK);

    }
    @PutMapping("/{id}/status")
    public Response<BankDto> toggleBankStatus(@PathVariable Long id) {

        Bank bank = bankService.toggleBankStatus(id);
        return new Response<BankDto>().buildSuccessResponse("Bank Status Updated Successfully",
                BankDto.of(bank), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<?> deleteBank(@PathVariable Long id) {

        bankService.deleteBank(id);
        return new Response<>().buildSuccessResponse("Bank Deleted Successfully",HttpStatus.OK);
    }
}
