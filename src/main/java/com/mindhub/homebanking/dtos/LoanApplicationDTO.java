package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private Long loanID;
    private Double amount;
    private Integer payments;
    private String accountDestiny;

    public  LoanApplicationDTO(){

    }
    public LoanApplicationDTO(Long loanID, Double amount, Integer payments, String accountDestiny) {
        this.loanID = loanID;
        this.amount = amount;
        this.payments = payments;
        this.accountDestiny = accountDestiny;
    }

    public Long getLoanID() {
        return loanID;
    }
    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getAccountDestiny() {
        return accountDestiny;
    }

}



