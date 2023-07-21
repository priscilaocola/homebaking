package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    List<LoanDTO> getLoanDTO();
    Loan findById(long id);
    Loan findByName(String string);
    void saveAll(List<Loan> loans);
    void saveLoan(Loan loan);
}
