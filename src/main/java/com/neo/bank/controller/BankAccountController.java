package com.neo.bank.controller;

import com.neo.bank.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankAccountController {
    private BankAccountService bankAccountService;

    @Autowired
    BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }
}
