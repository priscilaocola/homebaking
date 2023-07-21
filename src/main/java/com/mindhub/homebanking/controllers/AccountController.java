package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class AccountController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
     private TransactionService transactionService;
//    private String randomNumber(){
//        String randomNumber;
//
//        do {
//            int number = (int) (Math.random() * 899999 + 100000);
//            randomNumber = "VIN-" + number;
//        } while (accountService.findByNumber(randomNumber) != null);
//        return randomNumber;
//    }
    @GetMapping ("/accounts")
    public List<AccountDTO> getAllAccounts(){
        return accountService.getAccounts();

    }
   @GetMapping("/accounts/{id}")
    public AccountDTO getOneAccount(@PathVariable Long id){
        return accountService.getAccountID(id);
    }
    @PostMapping("/clients/current/accounts")

    public ResponseEntity<Object> newAccount(Authentication authentication, @RequestParam AccountType type){
        if (authentication.getName().isBlank()) {
            return new ResponseEntity<>("You need to be authenticated", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.findByEmail(authentication.getName());
        if (client == null) {
            return new ResponseEntity<>("Client doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (type == null) {
            return new ResponseEntity<>("Missing account type", HttpStatus.FORBIDDEN);
        }
            Account account = new Account("",  LocalDate.now(), 0.0, true, type);
            accountService.saveAccount(account);
            account.setNumber(accountService.randomNumberAccount());
            client.addAccount(account);
            accountService.saveAccount(account);
            clientService.saveClient(client);
            return new ResponseEntity<>("Account created :D",HttpStatus.CREATED);
        }
    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getActiveAccounts(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        List<Account> accounts = client.getAccounts().stream().filter(account -> account.isActive()).collect(Collectors.toList());
        return accounts.stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }
    @PatchMapping ("/clients/current/accounts")
    public ResponseEntity<Object> deleteAccount(Authentication authentication, @RequestParam String number) {
        if (authentication.getName() == null) {
            return new ResponseEntity<>("Client not authenticated", HttpStatus.FORBIDDEN);
        }
        if (number.isBlank()) {
            return new ResponseEntity<>("Account number incorrect", HttpStatus.FORBIDDEN);
        }
        Account account = accountService.findByNumber(number);
        List<Transaction> transactions = account.getTransactions().stream().filter(transaction -> transaction.isActive()).collect(Collectors.toList());
        if (account.getBalance() != 0) {
            return new ResponseEntity<>("Account balance must be 0 to delete", HttpStatus.FORBIDDEN);
        }
        transactions.forEach(transaction -> {transaction.setActive(false); transactionService.saveTransaction(transaction);});
        account.setActive(false);
        accountService.saveAccount(account);
        return new ResponseEntity<>("Account deleted", HttpStatus.OK);
    }

}





