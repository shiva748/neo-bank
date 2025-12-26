package com.neo.bank.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;

    @Column(nullable = false, unique = true)
    private String referenceId; // idempotency key

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type; // TRANSFER, DEPOSIT, WITHDRAWAL

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status; // INITIATED, COMPLETED, FAILED

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    private String description;

    // Who initiated the transaction
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_account_number")
    private BankAccount initiatorAccount;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
