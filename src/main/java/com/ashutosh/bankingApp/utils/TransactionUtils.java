package com.ashutosh.bankingApp.utils;

import com.ashutosh.bankingApp.dto.response.TransactionResponse;
import com.ashutosh.bankingApp.entity.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionUtils {
    public static List<TransactionResponse> getListOfTransactionResponse(List<Transaction> transactions){
        return transactions.stream()
                .map(transaction -> TransactionResponse.builder()
                        .amount(transaction.getAmount())
                        .transactionType(transaction.getTransactionType())
                        .transactionCreatedAt(
                                TransactionUtils.formattedDate(transaction.getTransactionCreatedAt())
                        )
                        .accountNumber(transaction.getAccountNumber())
                        .status(transaction.getTransactionStatus().toString())
                        .build()
                )
                .toList();
    }

    public static String formattedDate(LocalDateTime transactionCreatedAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
        return transactionCreatedAt.format(formatter); // Return the formatted date as a string
    }
}
