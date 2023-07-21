package com.mindhub.homebanking.dtos;

import java.util.List;

public class LoanCreateDTO {

    private String name;

    private double maxAmount;
    private List<Integer> payments;
    private Integer interests;
    public LoanCreateDTO() {
    }
    public LoanCreateDTO(String name, double maxAmount,List<Integer> payments, Integer  interests) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this. interests =  interests;
    }
    public String getName() {
        return name;
    }
    public List<Integer> getPayments() {
        return payments;
    }
    public double getMaxAmount() {
        return maxAmount;
    }
    public Integer getInterests() {
        return interests;
    }
}
