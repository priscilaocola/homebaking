package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


/**
 * Crea una tabla con el nombre de la Clase (la tabla que se va a mostrar en la base de datos)
 */
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDate creationDate;
    private double balance;
    private boolean isActive;
    private AccountType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private  Set<Transaction> transactions = new HashSet<>();

    public Account() {
    }
    public Account(String number, LocalDate creationDate, double balance,boolean isActive,AccountType type) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.isActive=isActive;
       this.type = type;
    }



    public String getNumber() {
        return number;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client){
        this.client = client;
    }


    /**
     * OBTENER LISTA DE TRANSACCIONES
     */
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }
    /**
     * AGREGAR TRANSACCION A DETERMINADA CUENTA Y AGREGAR TRANSACCION A LA LISTA DE TRANSACCIONES
     */
    public void addTransactions( Transaction transaction) {
        transactions.add(transaction);
        transaction.setAccount(this);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", creationDate=" + creationDate +
                ", balance=" + balance +
                ", client=" + client +
                '}';
    }


}

