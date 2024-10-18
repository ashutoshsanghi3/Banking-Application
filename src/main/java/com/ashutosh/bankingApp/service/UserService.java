package com.ashutosh.bankingApp.service;

import com.ashutosh.bankingApp.dto.request.CreditDebitRequest;
import com.ashutosh.bankingApp.dto.request.EnquiryRequest;
import com.ashutosh.bankingApp.dto.request.TransferRequest;
import com.ashutosh.bankingApp.dto.request.UserRequest;
import com.ashutosh.bankingApp.dto.response.BankResponse;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditDebitRequest creditRequest) throws AccountNotExistsException;

    BankResponse debitAccount(CreditDebitRequest debitRequest) throws AccountNotExistsException, InsufficientBalanceException;

    BankResponse transferFunds(TransferRequest transferRequest) throws AccountNotExistsException, InsufficientBalanceException;


    public class AccountNotExistsException extends RuntimeException {
        public AccountNotExistsException(String message) {
            super(message);
        }
    }

    public class InsufficientBalanceException extends RuntimeException {
        public InsufficientBalanceException(String message) {
            super(message);
        }
    }
}
