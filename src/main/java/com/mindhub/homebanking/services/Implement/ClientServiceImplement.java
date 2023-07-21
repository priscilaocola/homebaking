package com.mindhub.homebanking.services.Implement;


import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client findById (Long id){
        return clientRepository.findById(id).orElse(null);
    }
    @Override
    public List<ClientDTO> findAll() {
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }
    @Override
    public ClientDTO getClientAuthentication(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }



    @Override
    public ClientDTO getClientDTO (Long id) {
        return new ClientDTO(this.findById(id));
    }
    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }


    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }
}