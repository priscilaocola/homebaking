package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @RequestMapping("/clients")
    public List<ClientDTO> getClient(){
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable long id){
        return clientRepository.findById(id).map(client -> new ClientDTO(client)).orElse(null);
    }
}