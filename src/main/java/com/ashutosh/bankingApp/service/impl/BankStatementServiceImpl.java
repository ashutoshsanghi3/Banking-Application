package com.ashutosh.bankingApp.service.impl;

import com.ashutosh.bankingApp.dto.response.EmailDetails;
import com.ashutosh.bankingApp.dto.response.TransactionResponse;
import com.ashutosh.bankingApp.entity.Transaction;
import com.ashutosh.bankingApp.entity.User;
import com.ashutosh.bankingApp.repository.TransactionRepository;
import com.ashutosh.bankingApp.repository.UserRepository;
import com.ashutosh.bankingApp.service.BankStatementService;
import com.ashutosh.bankingApp.service.EmailService;
import com.ashutosh.bankingApp.service.TransactionService;
import com.ashutosh.bankingApp.service.UserService;
import com.ashutosh.bankingApp.utils.TransactionUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@Slf4j
public class BankStatementServiceImpl implements BankStatementService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    private static final String BANK_STATEMENT_FILE = System.getProperty("user.dir") + "/src/main/resources/bankingApp_bankStatement.pdf";

    @Override
    public List<TransactionResponse> generateTransactionStatement(String accountNumber, String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try{
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate,formatter);

            User user = userRepository.findByAccountNumber(accountNumber);
            if(user == null) {
                throw new UserService.AccountNotExistsException(accountNumber);
            }
            List<Transaction> transactions = transactionRepository
                    .findByUserAccountNumberOrderByTransactionCreatedAtDesc(accountNumber);
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getTransactionCreatedAt().isAfter(start.atStartOfDay()))
                    .filter(transaction -> transaction.getTransactionCreatedAt()
                            .isBefore(end.plusDays(1).atStartOfDay()))
                    .toList();

            designStatement(user,transactions, startDate,endDate);
            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(user.getEmail())
                    .subject("ACCOUNT STATEMENT")
                    .messageBody("Your account statement for the period " + startDate + " to " + endDate + " has been generated and attached as PDF.")
                    .attachment(BANK_STATEMENT_FILE)
                    .build();
            emailService.sendEmailAlertsWithAttachment(emailDetails);

            return TransactionUtils.getListOfTransactionResponse(transactions);
        }catch (DateTimeParseException ex){
            throw new TransactionService.InvalidDateFormatException(ex);
        } catch (DocumentException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void designStatement(User user, List<Transaction> transactions, String startDate, String endDate) throws FileNotFoundException, DocumentException {
        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("set size of document");
        OutputStream outputStream = new FileOutputStream(BANK_STATEMENT_FILE);
        PdfWriter.getInstance(document,outputStream);
        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA, 20f);

        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("Sanghi Franchise Bank",font));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.CYAN);
        bankName.setPadding(15f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("Bank Address : 34, New Horizon,Singapore"));
        bankAddress.setBorder(0);
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);
        bankInfoTable.setSpacingAfter(10f);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell startingDate = new PdfPCell(new Phrase("Start Date : "+startDate));
        startingDate.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);
        PdfPCell dueDate = new PdfPCell(new Phrase("End Date : "+endDate));
        dueDate.setBorder(0);
        PdfPCell userName = new PdfPCell(new Phrase("User Name : "+user.getFirstName()+" "+user.getLastName()));
        userName.setBorder(0);
        PdfPCell space1 = new PdfPCell();
        space1.setBorder(0);
        PdfPCell address = new PdfPCell(new Phrase("Address : "+user.getAddress()));
        address.setBorder(0);
        PdfPCell space2 = new PdfPCell();
        space2.setBorder(0);
        PdfPCell state = new PdfPCell(new Phrase("State : "+user.getStateOfOrigin()));
        state.setBorder(0);
        statementInfo.addCell(startingDate);
        statementInfo.addCell(statement);
        statementInfo.addCell(dueDate);
        statementInfo.addCell(userName);
        statementInfo.addCell(space1);
        statementInfo.addCell(address);
        statementInfo.addCell(space2);
        statementInfo.addCell(state);
        statementInfo.setSpacingAfter(10f);

        PdfPTable transactionsTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBorder(0);
        date.setBackgroundColor(BaseColor.CYAN);
        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBackgroundColor(BaseColor.CYAN);
        transactionType.setBorder(0);
        PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
        transactionAmount.setBackgroundColor(BaseColor.CYAN);
        transactionAmount.setBorder(0);
        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        status.setBackgroundColor(BaseColor.CYAN);
        status.setBorder(0);
        transactionsTable.addCell(date);
        transactionsTable.addCell(transactionType);
        transactionsTable.addCell(transactionAmount);
        transactionsTable.addCell(status);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        transactions.forEach(transaction -> {
            transactionsTable.addCell(new Phrase(transaction.getTransactionCreatedAt().format(formatter)));
            transactionsTable.addCell(new Phrase(transaction.getTransactionType()));
            transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
            transactionsTable.addCell(new Phrase(transaction.getTransactionStatus().toString()));
        });

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionsTable);

        document.close();
    }
}
