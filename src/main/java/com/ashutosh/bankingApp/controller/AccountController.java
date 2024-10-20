package com.ashutosh.bankingApp.controller;

import com.ashutosh.bankingApp.dto.request.CreditDebitRequest;
import com.ashutosh.bankingApp.dto.request.EnquiryRequest;
import com.ashutosh.bankingApp.dto.request.TransferRequest;
import com.ashutosh.bankingApp.dto.response.BankResponse;
import com.ashutosh.bankingApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    UserService userService;

    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.balanceEnquiry(enquiryRequest);
    }

    @PatchMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest creditRequest) throws UserService.AccountNotExistsException {
        return userService.creditAccount(creditRequest);
    }
    @PatchMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest debitRequest) throws UserService.InsufficientBalanceException, UserService.AccountNotExistsException {
        return userService.debitAccount(debitRequest);
    }

    @PatchMapping("/transfer")
    public BankResponse transferFunds(@RequestBody TransferRequest transferRequest)
            throws UserService.InsufficientBalanceException, UserService.AccountNotExistsException{
        return userService.transferFunds(transferRequest);
    }
}
