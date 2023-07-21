package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
    List<TransactionDTO> findByAccountAndDateBetween(Account account, LocalDateTime startDate, LocalDateTime endDate);
}

