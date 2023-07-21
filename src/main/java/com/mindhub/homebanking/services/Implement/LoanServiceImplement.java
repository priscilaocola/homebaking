package com.mindhub.homebanking.services.Implement;


import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImplement implements LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Override
    public List<LoanDTO> getLoanDTO() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }

    @Override
    public Loan findById(long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public Loan findByName(String string) {
        return loanRepository.findByName(string);
    }

    @Override
    public void saveAll(List<Loan> loans) {
        for (Loan loan : loans) {
            this.saveLoan(loan);
        }
    }

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }
}
