package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private long loanID;
    private double amount;
    private Integer payments;
    private String accountDestiny;

    public  LoanApplicationDTO(){

    }
    public LoanApplicationDTO(long loanID, double amount, Integer payments, String accountDestiny) {
        this.loanID = loanID;
        this.amount = amount;
        this.payments = payments;
        this.accountDestiny = accountDestiny;
    }

    public long getLoanID() {
        return loanID;
    }
    public double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getAccountDestiny() {
        return accountDestiny;
    }

}



