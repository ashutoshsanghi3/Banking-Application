package com.ashutosh.bankingApp.controller;

import com.ashutosh.bankingApp.dto.response.TransactionResponse;
import com.ashutosh.bankingApp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @GetMapping("")
    public List<TransactionResponse> getAllTransactions(@RequestParam String accountNumber) {
        return transactionService.getAllTransactions(accountNumber);
    }

    @GetMapping("/bankStatements")
    public List<TransactionResponse> getBankStatements(@RequestParam String accountNumber,
                                               @RequestParam String startDate,
                                               @RequestParam String endDate) {
        return transactionService.generateTransactionStatement(accountNumber,startDate,endDate);
    }
}
