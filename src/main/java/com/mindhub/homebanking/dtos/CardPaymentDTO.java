package com.mindhub.homebanking.dtos;


public class CardPaymentDTO {
    private String number;
    private Double amount;
    private Integer cvv;
    private String description;
    public CardPaymentDTO() {
    }
    public String getNumber() {
        return number;
    }
    public Double getAmount() {
        return amount;
    }
    public Integer getCvv() {
        return cvv;
    }
    public String getDescription() {
        return description;
    }
}