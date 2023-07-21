package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDate;

public class CardDTO {
    private long id;
    private String cardHolder;
    private CardType type;
    private CardColor color;
    private String number;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private int cvv;
   private boolean active;


    public CardDTO (){}

    public CardDTO (Card card){
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.cvv = card.getCvv();
       this.active = card.getActive();

    }

    public long getId() {return id;}

    public String getCardHolder() {return cardHolder;}

    public CardType getType() {return type;}

    public CardColor getColor() {return color;}

    public String getNumber() {return number;}

    public LocalDate getFromDate() {return fromDate;}

    public LocalDate getThruDate() {return thruDate;}

    public int getCvv() {return cvv;}

   public boolean getActive() {return active;
  }
}

