package com.mindhub.homebanking.services.Implement;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.utils.UtilsAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Override
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(accounts -> new AccountDTO(accounts)).collect(Collectors.toList());
    }

    @Override
    public AccountDTO getAccountID(Long id) {
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }


    @Override
    public String randomNumberAccount() {
        String randomNumber;
        do {
            int number = UtilsAccount.randomNumberAccount();
            randomNumber = "VIN-" + number;
        } while (findByNumber(randomNumber) != null);
        return randomNumber;
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }
}