package com.ashutosh.bankingApp.service;

import com.ashutosh.bankingApp.dto.internal.TransactionDto;
import com.ashutosh.bankingApp.dto.response.TransactionResponse;
import com.ashutosh.bankingApp.entity.Transaction;

import java.time.format.DateTimeParseException;
import java.util.List;

public interface TransactionService {
    Transaction saveTransaction(TransactionDto transactionDto);

    List<TransactionResponse> getAllTransactions(String accountNumber);

    List<TransactionResponse> generateTransactionStatement(String accountNumber, String startDate, String endDate);

    public class InvalidDateFormatException extends RuntimeException {
        public InvalidDateFormatException(DateTimeParseException message) {
            super(message);
        }
    }
}
