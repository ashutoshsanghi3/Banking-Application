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
                .transactionStatus(transactionDto.getTransactionStatus())
                .user(userRepository.findByAccountNumber(transactionDto.getAccountNumber()))
                .build();
        System.out.println("Transaction saved successfully");
        return transactionRepository.save(transaction);

    }

    @Override
    public List<TransactionResponse> getAllTransactions(String accountNumber) {
        List<Transaction> transactions = transactionRepository
                                        .findByUserAccountNumberOrderByTransactionCreatedAtDesc(accountNumber);
        transactions = transactions.stream()
                .toList();
        return TransactionUtils.getListOfTransactionResponse(transactions);
    }



}
