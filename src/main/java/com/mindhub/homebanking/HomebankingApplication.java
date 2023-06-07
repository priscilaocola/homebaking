package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository AccountRepository){
		return args -> {

			Client client1= new Client("Melba","Morel","melba@mindhub.com");
			Client client2= new Client("Dorian", "Ocola","dorian@gmail.com");


			Account account1 = new Account("VIN001", LocalDate.now(),  5000.0);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1),7500.0);
			Account account3 = new Account("VIN003", LocalDate.now().plusDays(2),500000.00);
			Account account4 = new Account("VIN004", LocalDate.now().plusDays(3),1050000.00);


			clientRepository.save(client1);
			client1.addAccount(account1);
			client1.addAccount(account2);
			AccountRepository.save(account1);
			AccountRepository.save(account2);
			clientRepository.save(client2);
			client2.addAccount(account3);
			client2.addAccount(account4);
			AccountRepository.save(account3);
			AccountRepository.save(account4);
		};

	}

};
