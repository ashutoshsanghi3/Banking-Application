package com.ashutosh.bankingApp.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionUtils {
    public static String formattedDate(LocalDateTime transactionCreatedAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
        return transactionCreatedAt.format(formatter); // Return the formatted date as a string
    }
}
