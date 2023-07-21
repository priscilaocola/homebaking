package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.ClientLoan;

public interface ClientLoanService {
    ClientLoan getClientLoan(Long id);
    void saveClientLoan(ClientLoan clientLoan);
    ClientLoan findById(Long id);
}
