package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {
    List<AccountDTO> getAccounts();

    AccountDTO getAccountID(Long id);
    Account findByNumber(String number);
    Account findById(long id);
    String randomNumberAccount();
    void saveAccount(Account account);
}
