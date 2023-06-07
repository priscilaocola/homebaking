package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDate;

public class AccountDTO {

    private long id;
    private String number;
    private LocalDate creationDate;
    private Double balance;

    public AccountDTO( Account account){
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
    }

    public String getNumber() {
        return number;
    }
    public LocalDate getCreationDate() {
        return creationDate;
    }
    public Double getBalance() {
        return balance;
    }
    public long getId() {
        return id;
    }

}
