package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;



@RequestMapping("/api")
@RestController
public class AccountController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

    private String randomNumber(){
        String randomNumber;

        do {
            int number = (int) (Math.random() * 899999 + 100000);
            randomNumber = "VIN-" + number;
        } while (accountService.findByNumber(randomNumber) != null);
        return randomNumber;
    }
    @RequestMapping ("/accounts")
    public List<AccountDTO> getAllAccounts(){
        return accountService.getAccounts();

    }
    @RequestMapping("/accounts/{id}")
    public AccountDTO getOneAccount(@PathVariable Long id){
        return accountService.getAccountID(id);
    }
    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)

    public ResponseEntity<Object> newAccount(Authentication authentication){
        if (clientService.findByEmail(authentication.getName()).getAccounts().size() <= 2){
            String accountNumber = randomNumber();
            Account newAccount = new Account(accountNumber, LocalDate.now(), 0.0);
         clientService.findByEmail(authentication.getName()).addAccount(newAccount);
         accountService.saveAccount(newAccount);
        }else{
            return  new ResponseEntity<>("You reached the limit of accounts", HttpStatus.FORBIDDEN);
        }
        return  new ResponseEntity<>(HttpStatus.CREATED);

    }

}





