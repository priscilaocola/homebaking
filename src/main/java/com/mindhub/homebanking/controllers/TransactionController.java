package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {

//    @Autowired
//    private ClientRepository clientRepository;
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private TransactionRepository transactionRepository;
@Autowired
private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)

    public ResponseEntity<Object> newTransaction (Authentication authentication
            , @RequestParam double amount, @RequestParam String description
            , @RequestParam String accountO, @RequestParam String accountD ){

        Client client = clientService.findByEmail(authentication.getName());
        Account selectAccountO = accountService.findByNumber(accountO.toUpperCase());
        Account selectAccountD= accountService.findByNumber(accountD.toUpperCase());
        Set<Account> accountExist = client.getAccounts().stream().filter(account -> account.getNumber().equals(accountO)).collect(Collectors.toSet());


        if (amount < 1){
            return new ResponseEntity<>("You  cannot send negative balance", HttpStatus.EXPECTATION_FAILED);
        }
        if (description.isBlank()){
            return new ResponseEntity<>("Please add a description", HttpStatus.FORBIDDEN);
        }
        if (selectAccountO == null){
            return new ResponseEntity<>("This account does not exist", HttpStatus.FORBIDDEN);
        }
        if (selectAccountO.equals(selectAccountD)){
            return new ResponseEntity<>("You cannot send money to the same account", HttpStatus.FORBIDDEN);
        }
        if (selectAccountD == null){
            return new ResponseEntity<>("This account does not exist", HttpStatus.FORBIDDEN);
        }
        if (selectAccountO.getBalance() < amount){
            return new ResponseEntity<>("you don't have enough balance", HttpStatus.FORBIDDEN);
        }
        if (client.getAccounts()
                .stream()
                .filter(account -> account.getNumber().equalsIgnoreCase(accountO)).collect(Collectors.toList()).size() == 0){
            return new ResponseEntity<>("This account is not yours", HttpStatus.FORBIDDEN);
        }
        Transaction transactionOrigin = new Transaction(Double.parseDouble("-" + amount), description, LocalDateTime.now(),TransactionType.DEBIT);
        Transaction transactionDestin = new Transaction(amount, description, LocalDateTime.now(),TransactionType.CREDIT);

        selectAccountO.addTransactions(transactionOrigin);
        selectAccountD.addTransactions(transactionDestin);

        selectAccountO.setBalance(selectAccountO.getBalance() - amount);
        selectAccountD.setBalance(selectAccountD.getBalance() + amount);

        transactionService.saveTransaction(transactionOrigin);
        transactionService.saveTransaction(transactionDestin);



        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}