package com.mindhub.homebanking.services.Implement;


import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
    @Override
    public List<TransactionDTO> findByAccountAndDateBetween(Account account, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByAccountAndDateBetween(account, startDate, endDate).stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toList());
    }
}