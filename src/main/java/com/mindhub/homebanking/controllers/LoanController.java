package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanCreateDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController

public class LoanController {

     @Autowired
     private ClientService clientService;
     @Autowired
     private AccountService accountService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoanDTO(){
        return loanService.getLoanDTO();
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> newLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){

        Client client = clientService.findByEmail(authentication.getName());
        if ( loanApplicationDTO.getLoanID() == null  ||loanApplicationDTO.getAmount() == null || loanApplicationDTO.getPayments() == null|| loanApplicationDTO.getAccountDestiny() == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        Account account = accountService.findByNumber(loanApplicationDTO.getAccountDestiny());
        Loan loanType = loanService.findById(loanApplicationDTO.getLoanID());

        Set<ClientLoan> clientLoans;

        if (loanType == null) {
            return new ResponseEntity<>("Incorrect loan type", HttpStatus.FORBIDDEN);
        }
        if (account == null) {
            return new ResponseEntity<>("Account used to apply to the loan do not exist", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() <= 9999) {
            return new ResponseEntity<>("Insufficient amount", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getPayments() <= 0) {
            return new ResponseEntity<>("Incorrect payments amount", HttpStatus.FORBIDDEN);
        }
        if (!loanType.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("That number of payments is not allowed", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() > loanType.getMaxAmount()) {
            return new ResponseEntity<>("Exceeded the loan maximun amount", HttpStatus.FORBIDDEN);
        }
        if (client.getAccounts().stream().noneMatch(loan1 -> loan1.getNumber().equals(loanApplicationDTO.getAccountDestiny()))) {
            return new ResponseEntity<>("Account do not belongs to the client", HttpStatus.FORBIDDEN);
        }
        double plusPercentage = (loanApplicationDTO.getAmount() * loanType.getInterests() / 100) + (loanApplicationDTO.getAmount());
        ClientLoan requestedLoan = new ClientLoan(plusPercentage, loanApplicationDTO.getPayments(), loanApplicationDTO.getPayments(),plusPercentage);
      clientLoanService.saveClientLoan(requestedLoan);

        Transaction newTransaction = new Transaction(loanApplicationDTO.getAmount(),loanType.getName() + " loan approved",  LocalDateTime.now(),TransactionType.CREDIT,true,account.getBalance());
        transactionService.saveTransaction(newTransaction);
        account.setBalance( account.getBalance() + loanApplicationDTO.getAmount());
        account.addTransactions(newTransaction);
        accountService.saveAccount(account);

       loanType.addClientLoan(requestedLoan);
        client.addClientLoan(requestedLoan);
      clientService.saveClient(client);


        return new ResponseEntity<>("Loan approved", HttpStatus.CREATED);
    }
    @PostMapping("/loans/create")
    public ResponseEntity<Object> createLoan(Authentication authentication, @RequestBody LoanCreateDTO loanCreateDTO) {
        if (authentication.getName() == null || loanCreateDTO == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (loanService.findByName(loanCreateDTO.getName()) != null) {
            return new ResponseEntity<>("Loan name is used", HttpStatus.FORBIDDEN);
        }
        List<Integer> orderedList = loanCreateDTO.getPayments().stream().sorted().collect(Collectors.toList());

        Loan newLoan = new Loan(loanCreateDTO.getName(),loanCreateDTO.getMaxAmount(),orderedList,loanCreateDTO.getInterests());
        loanService.saveLoan(newLoan);
        return new ResponseEntity<>("Loan type correctly added", HttpStatus.CREATED);
    }
    @GetMapping("/clients/current/loans")
    public List<ClientLoanDTO> getLoans(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        return client.getClientLoans().stream().map(loan -> new ClientLoanDTO(loan)).collect(Collectors.toList());
    }

    }


