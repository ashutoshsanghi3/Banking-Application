package com.ashutosh.bankingApp.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private String transactionType;
    private BigDecimal amount;
    private String transactionCreatedAt;
    private String accountNumber;
    @Enumerated(EnumType.STRING)
    private String status;
}
