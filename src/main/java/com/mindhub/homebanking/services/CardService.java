package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CardService {
    List<CardDTO> getCard(Authentication authentication);
    Card findByNumber(String number);
    Card findByCvv(int cvv);
    String randomNumberCards();
    String cardNumberNotRepeat();
    int randomCvv();
    Card findById(long id);
    void saveCard(Card card);
}
