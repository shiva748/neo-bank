package com.neo.bank.controller;

import com.neo.bank.entity.BankAccount;
import com.neo.bank.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class BankAccountController {
    private BankAccountService bankAccountService;

    @Autowired
    BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/myaccounts")
    public ResponseEntity<?> getMyAccounts(Authentication authentication) {
        return bankAccountService.myAccounts(authentication);
    }
}
