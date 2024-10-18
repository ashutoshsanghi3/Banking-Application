package com.ashutosh.bankingApp.service.impl;

import com.ashutosh.bankingApp.dto.internal.TransactionDto;
import com.ashutosh.bankingApp.dto.response.TransactionResponse;
import com.ashutosh.bankingApp.entity.Transaction;
import com.ashutosh.bankingApp.repository.TransactionRepository;
import com.ashutosh.bankingApp.repository.UserRepository;
import com.ashutosh.bankingApp.service.TransactionService;
import com.ashutosh.bankingApp.utils.TransactionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Transaction saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .amount(transactionDto.getAmount())
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .status("SUCCESS")
                .user(userRepository.findByAccountNumber(transactionDto.getAccountNumber()))
                .build();
        System.out.println("Transaction saved successfully");
        return transactionRepository.save(transaction);

    }
    private List<TransactionResponse> getListofTransacationResponse(List<Transaction> transactions){
        return transactions.stream()
               .map(transaction -> TransactionResponse.builder()
                       .amount(transaction.getAmount())
                       .transactionType(transaction.getTransactionType())
                       .transactionCreatedAt(
                                TransactionUtils.formattedDate(transaction.getTransactionCreatedAt())
                        )
                       .build()
                )
               .toList();
    }
    @Override
    public List<TransactionResponse> getAllTransactions(String accountNumber) {
        List<Transaction> transactions = transactionRepository
                                        .findByUserAccountNumberOrderByTransactionCreatedAtDesc(accountNumber);
        transactions = transactions.stream()
                .filter(transaction -> "SUCCESS".equals(transaction.getStatus()))
                .toList();
        return getListofTransacationResponse(transactions);
    }

    @Override
    public List<TransactionResponse> generateTransactionStatement(String accountNumber, String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try{
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate,formatter);

            List<Transaction> transactions = transactionRepository
                    .findByUserAccountNumberOrderByTransactionCreatedAtDesc(accountNumber);
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getTransactionCreatedAt().isAfter(start.atStartOfDay()))
                    .filter(transaction -> transaction.getTransactionCreatedAt()
                            .isBefore(end.plusDays(1).atStartOfDay()))
                    .toList();
            return getListofTransacationResponse(transactions);
        }catch (DateTimeParseException ex){
            throw new InvalidDateFormatException(ex);
        }


    }

}
