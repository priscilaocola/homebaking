package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
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
import java.util.stream.Collectors;



@RequestMapping("/api")
@RestController
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    private String randomNumber(){
        String randomNumber;

        do {
            int number = (int) (Math.random() * 899999 + 100000);
            randomNumber = "VIN-" + number;
        } while (accountRepository.findByNumber(randomNumber) != null);
        return randomNumber;
    }
    @RequestMapping ("/accounts")
    public List<AccountDTO> getAllAccounts(){
        return accountRepository.findAll()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(Collectors.toList());
    }
    @RequestMapping("/accounts/{id}")
    public AccountDTO getOneAccount(@PathVariable Long id){
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }
    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)

    public ResponseEntity<Object> newAccount(Authentication authentication){
        if (clientRepository.findByEmail(authentication.getName()).getAccounts().size() <= 2){
            String accountNumber = randomNumber();
            Account newAccount = new Account(accountNumber, LocalDate.now(), 0.0);
            clientRepository.findByEmail(authentication.getName()).addAccount(newAccount);
            accountRepository.save(newAccount);
        }else{
            return  new ResponseEntity<>("You reached the limit of accounts", HttpStatus.FORBIDDEN);
        }
        return  new ResponseEntity<>(HttpStatus.CREATED);

    }

}





