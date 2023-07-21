package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.CardPaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController

public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;

//    private String randomNumberCards(){
//        String randomCards = "";
//        for (int i = 0; i < 4; i++){
//            int min = 1000;
//            int max = 8999;
//            randomCards += (int) (Math.random() * max + min) + " ";
//        }
//        return randomCards;
//    }
//    private int randomCvv(){
//        int cvvRandom = (int)(Math.random()*899 + 100);
//        return cvvRandom;
//    }
    @GetMapping("/clients/current/cards")
    public List<CardDTO> getAccounts (Authentication authentication){
        return cardService.getCard(authentication);
    }

    @PostMapping("/clients/current/cards")

    public ResponseEntity<Object> addCard(Authentication authentication, @RequestParam CardType type , @RequestParam CardColor color){

        if (type  == null || color  == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        String cardsNumber = cardService.cardNumberNotRepeat();

        Client client = clientService.findByEmail(authentication.getName());

        Set<Card> cards = client.getCards().stream().filter(card -> card.getType() == type && card.getActive()).collect(Collectors.toSet());

        int cardsTotal = client.getCards().size();
        int cardsActive = (int) client.getCards().stream().filter(card -> card.getActive()).count();


        if (cardsTotal >= 8 || cardsActive >=8 ) {
            return new ResponseEntity<>("You reached the maximum number of cards you can have", HttpStatus.FORBIDDEN);
        }
        if (cards.stream().anyMatch(card -> card.getColor() == color && card.getActive() )){
            return new ResponseEntity<>("You can't have same cards", HttpStatus.FORBIDDEN);
        }
        Card newCard = new Card(type,color,cardService.cardNumberNotRepeat(),LocalDate.now(),LocalDate.now().plusYears(5),cardService.randomCvv(),client.getFirstName() + " " + client.getLastName(),true);
        clientService.findByEmail(authentication.getName()).addCard(newCard);
         cardService.saveCard(newCard);


        return  new ResponseEntity<>( HttpStatus.CREATED);
    }


    @PutMapping(path = "/clients/current/cards/{id}")
    public ResponseEntity<Object> cardDelete (Authentication authentication, @PathVariable long id){
        Client clientAuthentication = clientService.findByEmail(authentication.getName());
        Card cardID = cardService.findById(id);

        if(!cardID.getActive()){
            return new ResponseEntity<>("This card is deleted", HttpStatus.FORBIDDEN);
        }
        if (!clientAuthentication.getCards().contains(cardID)){
            return new ResponseEntity<>("This card does not belong to you", HttpStatus.FORBIDDEN);
        }
        if (cardID == null){
            return new ResponseEntity<>("ID does not exist", HttpStatus.FORBIDDEN);
        }

        cardID.setActive(false);
        cardService.saveCard(cardID);

        return new ResponseEntity<>("The card was deleted", HttpStatus.ACCEPTED);
    }
    @PostMapping("/cards/renew")
    public ResponseEntity<Object> renewCard(Authentication authentication, @RequestParam String number) {
        if (authentication.getName().isBlank()) {
            return new ResponseEntity<>("Client must be authenticated",HttpStatus.FORBIDDEN);
        }
        if (number.isBlank()) {
            return  new ResponseEntity<>("Error, try again!", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.findByEmail(authentication.getName());
        Card card = cardService.findByNumber(number);
        if (!client.getCards().contains(card)) {
            return new ResponseEntity<>("Not your card!", HttpStatus.FORBIDDEN);
        }
        if (!card.getThruDate().isBefore(LocalDate.now())) {
            return new ResponseEntity<>("Card is not expired!", HttpStatus.FORBIDDEN);
        }
        card.setActive(false);
        cardService.saveCard(card);
       addCard(authentication,card.getType(), card.getColor() );
        return new ResponseEntity<>("Card renew", HttpStatus.OK);
    }
    @Transactional
    @CrossOrigin(origins = "http://127.0.0.1:5501")
    @PostMapping("/cards/payments")
    public ResponseEntity<?> Payment(@RequestBody CardPaymentDTO cardPaymentDTO) {
        if (cardPaymentDTO.getNumber().length() != 19) {
            return new ResponseEntity<>("Invalid Card Number", HttpStatus.FORBIDDEN);
        }
        if (cardPaymentDTO.getAmount() <= 0) {
            return new ResponseEntity<>("Invalid Amount", HttpStatus.FORBIDDEN);
        }
        if (cardPaymentDTO.getCvv().toString().length() < 3) {
            return new ResponseEntity<>("Invalid Cvv", HttpStatus.FORBIDDEN);
        }
        if (cardPaymentDTO.getDescription().isBlank()) {
            return new ResponseEntity<>("Empty Description", HttpStatus.FORBIDDEN);
        }
        Card card = cardService.findByNumber(cardPaymentDTO.getNumber());
        if (card.getType() != CardType.DEBIT) {
            return new ResponseEntity<>("Not a debit card", HttpStatus.FORBIDDEN);
        }
        if (card.getThruDate().isBefore(LocalDate.now())) {
            return new ResponseEntity<>("Card is expired!", HttpStatus.FORBIDDEN);
        }
        if (!String.valueOf(card.getCvv()).equals(String.valueOf(cardPaymentDTO.getCvv()))) {
            return new ResponseEntity<>("Invalid Cvv", HttpStatus.FORBIDDEN);
        }
        Account account = card.getClient().getAccounts().stream().filter(account1 -> account1.getBalance() > cardPaymentDTO.getAmount()).collect(Collectors.toList()).get(0);
        if (account.getBalance() < cardPaymentDTO.getAmount()) {
            return new ResponseEntity<>("Insufficient money in your account", HttpStatus.FORBIDDEN);
        }
        Transaction newTransaction = new Transaction(cardPaymentDTO.getAmount(), "Payment: " + cardPaymentDTO.getDescription(),  LocalDateTime.now(),TransactionType.DEBIT,  true,account.getBalance() - cardPaymentDTO.getAmount());
        account.addTransactions(newTransaction);
        account.setBalance(account.getBalance() - cardPaymentDTO.getAmount());
        transactionService.saveTransaction(newTransaction);
        accountService.saveAccount(account);
        return new ResponseEntity<>("Payment Complete", HttpStatus.OK);
    }
    }
