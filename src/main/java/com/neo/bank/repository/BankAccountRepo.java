package com.neo.bank.repository;

import com.neo.bank.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BankAccountRepo extends JpaRepository<BankAccount, UUID> {

}
