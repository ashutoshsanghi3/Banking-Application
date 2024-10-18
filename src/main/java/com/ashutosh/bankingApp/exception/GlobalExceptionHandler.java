package com.ashutosh.bankingApp.exception;

import com.ashutosh.bankingApp.dto.response.BankResponse;
import com.ashutosh.bankingApp.service.EmailService;
import com.ashutosh.bankingApp.service.TransactionService;
import com.ashutosh.bankingApp.service.UserService;
import com.ashutosh.bankingApp.utils.AccountUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
//the above annotation will handle all exceptions globally for the entire application.
public class GlobalExceptionHandler {

    // Handle Account Not Exists
    @ExceptionHandler(UserService.AccountNotExistsException.class)
    public ResponseEntity<BankResponse> handleAccountExistsException(UserService.AccountNotExistsException ex) {
        BankResponse response = BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                .responseMessage(ex.getMessage()+AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT); // Or appropriate status
    }

    // Handle Insufficient Balance
    @ExceptionHandler(UserService.InsufficientBalanceException.class)
    public ResponseEntity<BankResponse> handleInsufficientBalanceException(UserService.InsufficientBalanceException ex) {
        BankResponse response = BankResponse.builder()
                .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                .responseMessage(ex.getMessage()+AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle email service error
    @ExceptionHandler(EmailService.EmailSendingException.class)
    public ResponseEntity<BankResponse> handleEmailSendingException(EmailService.EmailSendingException ex) {
        BankResponse response = BankResponse.builder()
                .responseCode(AccountUtils.EMAIL_NOT_SENT_CODE)
                .responseMessage(AccountUtils.EMAIL_NOT_SENT_MESSAGE + ex.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TransactionService.InvalidDateFormatException.class)
    public ResponseEntity<BankResponse> handleEmailSendingException(TransactionService.InvalidDateFormatException ex) {
        BankResponse response = BankResponse.builder()
                .responseCode(AccountUtils.INVALID_DATE_FORMAT_CODE)
                .responseMessage(AccountUtils.INVALID_DATE_FORMAT_MESSAGE)
                .build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
