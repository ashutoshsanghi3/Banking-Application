package com.ashutosh.bankingApp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreditDebitRequest {
    private String accountNumber;
    private BigDecimal amount;
}
