package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;
    private double amount;
    private String description;
    private LocalDateTime date;
    private TransactionType type;
   private boolean isActive;
    private double balanceTotal;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;
    public Transaction() {
    }

    public Transaction(double amount, String description, LocalDateTime date, TransactionType type,boolean isActive,double balanceTotal) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
       this.isActive=isActive;
       this.balanceTotal = balanceTotal;

    }

    public Long getId() {
        return id;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }
    public void setType(TransactionType type) {
        this.type = type;
    }
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    public double getBalanceTotal() {
        return balanceTotal;
    }

    public void setBalanceTotal(double balanceTotal) {
        this.balanceTotal = balanceTotal;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", account=" + account +
                '}';
    }



}








