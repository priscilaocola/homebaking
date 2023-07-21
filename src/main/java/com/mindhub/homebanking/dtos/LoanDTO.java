package com.mindhub.homebanking.dtos;


import com.mindhub.homebanking.models.Loan;

import java.util.ArrayList;
import java.util.List;

public class LoanDTO {
        private long id;
        private String name;
        private double maxAmount;
        private List<Integer> payments = new ArrayList<>();
       private Integer interests;

        public LoanDTO(Loan loan){
            this.id = loan.getId();
            this.name = loan.getName();
            this.maxAmount = loan.getMaxAmount();
            this.payments = loan.getPayments();
           this.interests = loan.getInterests();
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getMaxAmount() {
            return maxAmount;
        }

        public List<Integer> getPayments() {
            return payments;
        }
       public Integer getInterests() {return interests;
    }
    }

