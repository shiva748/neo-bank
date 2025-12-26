package com.neo.bank.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ledger_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ledgerEntryId;

    // Many ledger entries → one transaction
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    // Many ledger entries → one bank account
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "account_number",
            nullable = false,
            referencedColumnName = "accountNumber"
    )
    private BankAccount bankAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntryType entryType; // DEBIT or CREDIT

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
