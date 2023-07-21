package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {
    List<ClientDTO> findAll();
    ClientDTO getClientDTO(Long id);
    ClientDTO getClientAuthentication(Authentication authentication);
    Client findByEmail(String email);
    Client findById (Long id);
    void saveClient(Client client);
}
