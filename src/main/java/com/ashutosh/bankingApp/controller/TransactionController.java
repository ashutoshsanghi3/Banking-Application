package com.ashutosh.bankingApp.controller;

import com.ashutosh.bankingApp.dto.response.TransactionResponse;
import com.ashutosh.bankingApp.service.TransactionService;
import com.ashutosh.bankingApp.service.BankStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @Autowired
    BankStatementService bankStatementService;

    @GetMapping("/transactions")
    public List<TransactionResponse> getAllTransactions(@RequestParam String accountNumber) {
        return transactionService.getAllTransactions(accountNumber);
    }

    @GetMapping("/bankStatement")
    public List<TransactionResponse> getBankStatement(@RequestParam String accountNumber,
                                               @RequestParam String startDate,
                                               @RequestParam String endDate) {
        return bankStatementService.generateTransactionStatement(accountNumber,startDate,endDate);
    }
}
