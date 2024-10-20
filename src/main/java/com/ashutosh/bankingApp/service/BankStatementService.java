package com.ashutosh.bankingApp.service;

import com.ashutosh.bankingApp.dto.response.TransactionResponse;
import com.ashutosh.bankingApp.entity.Transaction;
import com.ashutosh.bankingApp.entity.User;
import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.util.List;

public interface BankStatementService {
    List<TransactionResponse> generateTransactionStatement(String accountNumber, String startDate, String endDate);

    void designStatement(User user, List<Transaction> transactions, String startDate, String endDate) throws FileNotFoundException, DocumentException;
}
