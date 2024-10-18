package com.ashutosh.bankingApp.repository;

import com.ashutosh.bankingApp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,String> {
    List<Transaction> findByUserAccountNumberOrderByTransactionCreatedAtDesc(String accountNumber);
}
