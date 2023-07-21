package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private Long id;
    private Long loanId;
    private String name;
    private Double amount;
    private Integer payments;
    private Integer remainPayments;
    private Double remainAmount;




    public ClientLoanDTO(ClientLoan clientLoan){
        this.id=clientLoan.getId();
        this.loanId=clientLoan.getLoan().getId();
        this.name= clientLoan.getLoan().getName();
        this.amount= clientLoan.getAmount();
        this.payments= clientLoan.getPayments();
        this.remainPayments = clientLoan.getRemainPayments();
        this.remainAmount = clientLoan.getRemainAmount();
    }


    public Long getId() {
        return id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getName() {
        return name;
    }

    public Integer getRemainPayments() {
        return remainPayments;
    }

    public Double getRemainAmount() {
        return remainAmount;
    }





    }

