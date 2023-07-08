package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/api")
public class LoanController {

//    private ClientRepository clientRepository;
//    @Autowired
//    private AccountRepository accountRepository;
//    @Autowired
//    private TransactionRepository transactionRepository;
//    @Autowired
//    private LoanRepository loanRepository;
//    @Autowired
//    private ClientLoanRepository clientLoanRepository;
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

    @RequestMapping("/loans")
    public List<LoanDTO> getLoanDTO(){
        return loanService.getLoanDTO();
    }
    @Transactional
    @RequestMapping(path = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> newLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){

        Client client = clientService.findByEmail(authentication.getName());
//        if (loanApplicationDTO.getLoanID() == null || loanApplicationDTO.getAmount() == null || loanApplicationDTO.getPayments() == null || loanApplicationDTO.getAccountDestiny() == null) {
//            return new ResponseEntity<>("missing data", HttpStatus.FORBIDDEN);
//        }
        Account account = accountService.findByNumber(loanApplicationDTO.getAccountDestiny().toUpperCase());
        Loan selectLoan =loanService.findById(loanApplicationDTO.getLoanID()).orElse(null);



        if (selectLoan == null) {
            return new ResponseEntity<>("The selected loan is not available", HttpStatus.FORBIDDEN);
        } 

        if (!selectLoan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("Installments not available for this loan", HttpStatus.FORBIDDEN);
        }

        if (Double.isNaN(loanApplicationDTO.getAmount())) {
            return new ResponseEntity<>("The amount entered is not valid", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getAmount() < 1000) {
            return new ResponseEntity<>("Cannot enter negative amounts", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getPayments() <= 0) {
            return new ResponseEntity<>("Payments amount incorrect", HttpStatus.FORBIDDEN);
        }

        if (selectLoan.getMaxAmount() < loanApplicationDTO.getAmount()) {
            return new ResponseEntity<>("The amount requested exceeds the maximum amount available", HttpStatus.FORBIDDEN);
        }

        if (account == null) {
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("This account does not belong to an authenticated client", HttpStatus.FORBIDDEN);
        }

        ClientLoan requestedLoan = new ClientLoan(loanApplicationDTO.getAmount() + (loanApplicationDTO.getAmount() * 0.2), loanApplicationDTO.getPayments());
      clientLoanService.saveClientLoan(requestedLoan);

        Transaction newTransaction = new Transaction(loanApplicationDTO.getAmount(),selectLoan.getName() + " loan approved",  LocalDateTime.now(),TransactionType.CREDIT);
        transactionService.saveTransaction(newTransaction);
        account.setBalance( account.getBalance() + loanApplicationDTO.getAmount());
        account.addTransactions(newTransaction);
        accountService.saveAccount(account);

        selectLoan.addClientLoan(requestedLoan);
        client.addClientLoan(requestedLoan);
      clientService.saveClient(client);


        return new ResponseEntity<>("Loan approved", HttpStatus.CREATED);
    }
    }


