package com.neo.bank.service;

import com.neo.bank.dto.ApiResponse;
import com.neo.bank.dto.SimpleResponse;
import com.neo.bank.entity.AccountStatus;
import com.neo.bank.entity.AccountType;
import com.neo.bank.entity.BankAccount;
import com.neo.bank.entity.Users;
import com.neo.bank.repository.BankAccountRepo;
import com.neo.bank.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {
    private BankAccountRepo bankAccountRepo;
    private UserRepo userRepo;
    @Autowired
    BankAccountService(BankAccountRepo bankAccountRepo, UserRepo userRepo) {
        this.bankAccountRepo = bankAccountRepo;
        this.userRepo = userRepo;
    }

    public ResponseEntity<?> myAccounts(Authentication authentication) {
        try {
            String username = authentication.getName();
            Optional<Users> user = Optional.ofNullable(userRepo.findByUsername(username));
            if (!user.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SimpleResponse(false, "User not found"));
            }
            List<BankAccount> bankAccounts = user.get().getBankAccounts();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(bankAccounts.size() == 0?"No Bank Account Found.":bankAccounts.size()+" Bank Account found", true, bankAccounts));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SimpleResponse(false, "Internal Server Error"));
        }
    }

    public ResponseEntity<?> createAccount(Authentication authentication, BankAccount bankAccount) {
        try {
            String username = authentication.getName();
            Optional<Users> user = Optional.ofNullable(userRepo.findByUsername(username));
            if (!user.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SimpleResponse(false, "User not found"));
            }
            bankAccount.setUser(user.get());
            bankAccount.setStatus(AccountStatus.Active);
            if(!bankAccount.isValid()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse(false, "Invalid Account"));
            }
            if(bankAccount.getAccountType() == AccountType.CURRENT && bankAccount.getBalance().longValue() < 10000){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse(false, "Minimum opening Balance for current account is 10000."));
            }
            BankAccount account = bankAccountRepo.save(bankAccount);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Account Created", true, account));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse(false, "Internal Server Error"));
        }
    }

}
