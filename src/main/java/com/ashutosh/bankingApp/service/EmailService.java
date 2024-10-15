package com.ashutosh.bankingApp.service;

import com.ashutosh.bankingApp.dto.response.EmailDetails;

public interface EmailService {
    void sendEmailAlerts(EmailDetails emailDetails);

    public class EmailSendingException extends RuntimeException {
        public EmailSendingException(String message){
            super(message);
        }
    }
}
