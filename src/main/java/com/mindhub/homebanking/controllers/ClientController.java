package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@RequestMapping("/api")
@RestController
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

//    private String randomNumber(){
//        String randomNumber;
//
//        do {
//            int number = (int) (Math.random() * 999999 + 100000);
//            randomNumber = "VIN-" + number;
//        } while( accountRepository.findByNumber(randomNumber) != null);
//        return randomNumber;
//    }

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientService.findAll();
    }
    @GetMapping("/clients/{id}")
   public ClientDTO getOneClient(@PathVariable Long id){
      return clientService.getClientDTO(id);
    }
    @GetMapping  ("/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        return clientService.getClientAuthentication(authentication);
    }
    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password
    ){

        if (firstName.isBlank()) {
            return new ResponseEntity<>("the first name is missing", HttpStatus.FORBIDDEN);
        }
        else if (lastName.isBlank()) {
            return new ResponseEntity<>("Your last name is missing.", HttpStatus.FORBIDDEN);
        }
         else if (email.isBlank()) {
            return new ResponseEntity<>("Your email is missing.", HttpStatus.FORBIDDEN);
        }
       else  if (password.isBlank()) {
                return new ResponseEntity<> ("Your password is missing", HttpStatus.FORBIDDEN);
            }
       else  if(clientService.findByEmail(email) !=null) {
            return new ResponseEntity<>("Email already in use",HttpStatus.FORBIDDEN);
        }

        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
       clientService.saveClient(newClient);
        String accountNumber = accountService.randomNumberAccount();
        Account newAccount = new Account(accountNumber, LocalDate.now(), 0.0,true, AccountType.SAVINGS);
        newClient.addAccount(newAccount);
     accountService.saveAccount(newAccount);

        return new ResponseEntity<>( "registration completed successfully!",HttpStatus.CREATED);

        }



    }


