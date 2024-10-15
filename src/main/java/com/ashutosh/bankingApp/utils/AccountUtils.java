package com.ashutosh.bankingApp.utils;

import com.ashutosh.bankingApp.entity.User;
import com.ashutosh.bankingApp.repository.UserRepository;

import java.security.SecureRandom;
import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already exists!";

    public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account created successfully!";

    public static final String ACCOUNT_FOUND_CODE = "003";
    public static final String ACCOUNT_FOUND_MESSAGE = "User account found!";

    public static final String ACCOUNT_CREDITED_SUCCESS_CODE = "004";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "User account credited successfully";

    public static final String ACCOUNT_DEBITED_SUCCESS_CODE = "005";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "User account debited successfully";

    public static final String TRANSFER_SUCCESS_CODE = "007";
    public static final String TRANSFER_SUCCESS_MESSAGE = "Transfer successfully completed";

    public static final String ACCOUNT_NOT_EXISTS_CODE = "101";
    public static final String ACCOUNT_NOT_EXISTS_MESSAGE = " account does not exist!";

    public static final String INSUFFICIENT_BALANCE_CODE = "102";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = " account has insufficient balance";

    public static final String EMAIL_NOT_SENT_CODE = "103";
    public static final String EMAIL_NOT_SENT_MESSAGE = "Failed to send email to: ";

    private static String generateAccountNumber(){
        /*
         * Account Number :- 2023 + randomSixDigits
         */
        Year currentYear = Year.now();

        /*
        * SecureRandom is designed for cryptographic purposes and generates less predictable random numbers,
        * making it much more secure for generating account numbers.
         */
        SecureRandom random = new SecureRandom();
        int min = 100000;
        int max = 999999;
        int randomSixDigits = random.nextInt((max - min) + 1) + min;
        /*
         *   Random Six Digits :- >=100000 and <=999999
         */

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randomSixDigits);

        return year + randomNumber;
    }
    public static String generateUniqueAccountNumber(UserRepository userRepository){
        String accountNumber;
        do{
            accountNumber = generateAccountNumber();

        }while(userRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    public static String findAccountName(User user) {
        if(user.getOtherName()==null){
            return user.getFirstName() + " " + user.getLastName();
        }else{
            return user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName();
        }
    }
}
