package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    private String randomNumberCards(){
        String randomCards = "";
        for (int i = 0; i < 4; i++){
            int min = 1000;
            int max = 8999;
            randomCards += (int) (Math.random() * max + min) + " ";
        }
        return randomCards;
    }
    private int randomCvv(){
        int cvvRandom = (int)(Math.random()*899 + 100);
        return cvvRandom;
    }
    @RequestMapping("/clients/current/cards")
    public List<CardDTO> getAccounts (Authentication authentication){
        return cardService.getCard(authentication);
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)

    public ResponseEntity<Object> addCard(Authentication authentication, @RequestParam CardType type , @RequestParam CardColor color){

        if (type  == null || color  == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        String cardsNumber;
        do {
            cardsNumber = randomNumberCards();
        }while(cardService.findByNumber(cardsNumber) != null);

        int cardsCvv;

        do {
            cardsCvv = randomCvv();
        }while (cardService.findByCvv(cardsCvv) != null);

        Client client = clientService.findByEmail(authentication.getName());

        for (Card card :client.getCards()) {
            if (card.getType().equals(type) && card.getColor().equals((color))) {
                return new ResponseEntity<>("You already have this color and type of card", HttpStatus.FORBIDDEN);
            }
        }

        Card newCard = new Card(type,color,randomNumberCards(),LocalDate.now(),LocalDate.now().plusYears(5),randomCvv(),client.getFirstName() + " " + client.getLastName());
        clientService.findByEmail(authentication.getName()).addCard(newCard);
         cardService.saveCard(newCard);


        return  new ResponseEntity<>( HttpStatus.CREATED);

    }
}
