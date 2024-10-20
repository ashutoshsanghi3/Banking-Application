package com.ashutosh.bankingApp.utils.enums;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    SUCCESS("Success"),
    FAILED("Failed");

    private final String transactionStatus;


    TransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
