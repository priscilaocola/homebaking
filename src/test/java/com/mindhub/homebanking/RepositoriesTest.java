//package com.mindhub.homebanking;
//
//
//import com.mindhub.homebanking.models.*;
//import com.mindhub.homebanking.repositories.*;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.List;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
//
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = NONE)
//public class RepositoriesTest {
//    @Autowired
//    ClientRepository clientRepository;
//    @Autowired
//    AccountRepository accountRepository;
//    @Autowired
//    CardRepository cardRepository;
//    @Autowired
//    TransactionRepository transactionRepository;
//    @Autowired
//    LoanRepository loanRepository;
//
//
//    @Test
//    public void existClient(){
//        List<Client> clients = clientRepository.findAll();
//        assertThat(clients, is(not(empty())));}
//
//    @Test
//    public void existClientName(){
//        List<Client> clients = clientRepository.findAll();
//        assertThat(clients, hasItem(hasProperty("firstName", is("Melba"))));}
//
//    @Test
//    public void existAccountBalance(){
//        List<Account> accounts = accountRepository.findAll();
//        assertThat(accounts,hasItem(hasProperty("balance",is(greaterThanOrEqualTo(0.0)))));}
//    @Test
//    public void exitstAccount() {
//        List<Account> accounts = accountRepository.findAll();
//        assertThat(accounts, is(not(empty())));
//    }
//    @Test
//    public void existCards(){
//        List<Card> cards = cardRepository.findAll();
//        assertThat(cards,is(not(empty())));
//    }
//    @Test
//    public void existCardHolder(){
//        List<Card> cards = cardRepository.findAll();
//        assertThat(cards, hasItem(hasProperty("cardHolder", is("Melba Morel"))));
//    }
//    @Test
//    public void existTransaction(){
//        List<Transaction> transactions = transactionRepository.findAll();
//        assertThat(transactions,is(not(empty())));
//    }
//    @Test
//    public void existTransactionDescription(){
//        List<Transaction> transactions = transactionRepository.findAll();
//        assertThat(transactions, hasItem(hasProperty("description", is("Car fee"))));
//    }
//    @Test
//    public void existPersonalLoan(){
//
//        List<Loan> loans = loanRepository.findAll();
//
//        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
//
//    }
//    @Test
//    public void existLoans(){
//
//        List<Loan> loans = loanRepository.findAll();
//
//        assertThat(loans,is(not(empty())));
//
//    }
//
//
//}

