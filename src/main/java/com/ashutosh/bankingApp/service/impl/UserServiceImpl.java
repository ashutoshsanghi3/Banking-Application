package com.ashutosh.bankingApp.service.impl;

import com.ashutosh.bankingApp.dto.request.CreditDebitRequest;
import com.ashutosh.bankingApp.dto.request.EnquiryRequest;
import com.ashutosh.bankingApp.dto.request.TransferRequest;
import com.ashutosh.bankingApp.dto.request.UserRequest;
import com.ashutosh.bankingApp.dto.response.AccountInfo;
import com.ashutosh.bankingApp.dto.response.BankResponse;
import com.ashutosh.bankingApp.dto.response.EmailDetails;
import com.ashutosh.bankingApp.entity.User;
import com.ashutosh.bankingApp.repository.UserRepository;
import com.ashutosh.bankingApp.service.EmailService;
import com.ashutosh.bankingApp.service.UserService;
import com.ashutosh.bankingApp.utils.AccountUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /*
        * Creating a account - saving a new user into db
        */

        //check if the user exists in db
        if(userRepository.existsByEmail(userRequest.getEmail())){
            System.out.println("User already exists");
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                /*
                * check for unique account number
                 */
                .accountNumber(AccountUtils.generateUniqueAccountNumber(userRepository))
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();

        // saving a new user into db
        User savedUser = userRepository.save(newUser);
        //find accountName where if it has other name or not
        String accountName=AccountUtils.findAccountName(savedUser);

        //Sending email alert for account creation
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Creation Email")
                .messageBody("Congratulations! Your account has been successfully created. \n" +
                        "Account Name: " + accountName + "\n" +
                        "Account Number : " + savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlerts(emailDetails);

        //Returning bank response
        AccountInfo newAccountInfo = AccountInfo.builder()
                .accountBalance(savedUser.getAccountBalance())
                .accountNumber(savedUser.getAccountNumber())
                .accountName(accountName)
                .build();
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(newAccountInfo)
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest){
        //checking if account does not exist
        if(!userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber())){
            System.out.println("User does not exist");
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        //fetching the account details from db
        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        String accountName=AccountUtils.findAccountName(foundUser);

        //Sending email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(foundUser.getEmail())
                .subject("Balance Enquiry Email")
                .messageBody("The details of your account are as follows \n" +
                        "Account Name: " + accountName + "\n" +
                        "Account Balance : " + foundUser.getAccountBalance())
                .build();

        emailService.sendEmailAlerts(emailDetails);

        //Sending bank response
        AccountInfo foundInfo = AccountInfo.builder()
                .accountBalance(foundUser.getAccountBalance())
                .accountNumber(foundUser.getAccountNumber())
                .accountName(accountName)
                .build();
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(foundInfo)
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        //checking if account does not exist
        if(!userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber())) {
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
        }

        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        String accountName=AccountUtils.findAccountName(foundUser);
        //Sending email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(foundUser.getEmail())
                .subject("Name Enquiry Email")
                .messageBody("The name of your account is as follows \n" +
                        "Account Name: " + accountName)
                .build();

        emailService.sendEmailAlerts(emailDetails);

        //Returning account name
        return accountName;
    }

    @Override
    @Transactional
    public BankResponse creditAccount(CreditDebitRequest creditRequest) throws AccountNotExistsException {
        //checking if account does not exist
        if (!userRepository.existsByAccountNumber(creditRequest.getAccountNumber())) {
            System.out.println("User does not exist");
            throw new AccountNotExistsException(creditRequest.getAccountNumber());
        }

        User userToCredit = userRepository.findByAccountNumber(creditRequest.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditRequest.getAmount()));
        //saving the updated balance to the account number
        userRepository.save(userToCredit);

        String accountName = AccountUtils.findAccountName(userToCredit);

        //Sending email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userToCredit.getEmail())
                .subject("CREDIT ALERT")
                .messageBody("Rs " + creditRequest.getAmount() + " has been credited from account " +
                        creditRequest.getAccountNumber())
                .build();

        emailService.sendEmailAlerts(emailDetails);

        //Sending bank response
        AccountInfo updatedInfo = AccountInfo.builder()
                .accountBalance(userToCredit.getAccountBalance())
                .accountNumber(userToCredit.getAccountNumber())
                .accountName(accountName)
                .build();
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(updatedInfo)
                .build();
    }

    @Override
    @Transactional
    public BankResponse debitAccount(CreditDebitRequest debitRequest)
            throws AccountNotExistsException, InsufficientBalanceException {
        //checking if account does not exist
        if (!userRepository.existsByAccountNumber(debitRequest.getAccountNumber())) {
            System.out.println("User does not exist");
            throw new AccountNotExistsException(debitRequest.getAccountNumber());
        }

        User userToDebit = userRepository.findByAccountNumber(debitRequest.getAccountNumber());


        // Check if the amount being debited is greater than the current balance
        if (userToDebit.getAccountBalance().compareTo(debitRequest.getAmount()) < 0) {
            System.out.println("Insufficient balance");
            throw new InsufficientBalanceException(debitRequest.getAccountNumber());
        }


        // Subtract the amount after confirming sufficient balance
        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(debitRequest.getAmount()));

        //saving the updated balance to the account number
        userRepository.save(userToDebit);

        String accountName = AccountUtils.findAccountName(userToDebit);

        //Sending email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userToDebit.getEmail())
                .subject("DEBIT ALERT")
                .messageBody("Rs " + debitRequest.getAmount() + " has been debited from account " +
                        debitRequest.getAccountNumber())
                .build();

        emailService.sendEmailAlerts(emailDetails);

        //Sending bank response
        AccountInfo updatedInfo = AccountInfo.builder()
                .accountBalance(userToDebit.getAccountBalance())
                .accountNumber(userToDebit.getAccountNumber())
                .accountName(accountName)
                .build();
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                .accountInfo(updatedInfo)
                .build();
    }

    @Override
    @Transactional
    //if debit is successful and credit is not then both operations will fail together and rollback
    public BankResponse transferFunds(TransferRequest transferRequest)
            throws AccountNotExistsException, InsufficientBalanceException {

        CreditDebitRequest debitRequest = CreditDebitRequest.builder()
                .accountNumber(transferRequest.getSenderAccountNumber())
                .amount(transferRequest.getAmount())
                .build();
        debitAccount(debitRequest);

        CreditDebitRequest creditRequest = CreditDebitRequest.builder()
                .accountNumber(transferRequest.getReceiverAccountNumber())
                .amount(transferRequest.getAmount())
                .build();
        creditAccount(creditRequest);

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
                .build();



    }
}
