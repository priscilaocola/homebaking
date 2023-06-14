package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class homebankingApplication {
	public static void main(String[] args) {
		SpringApplication.run(homebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData (ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository){
		return args -> {

			Client client1= new Client("Melba","Morel","melba@mindhub.com");
			Client client2= new Client("Dorian", "Ocola","dorian@gmail.com");

			Account account1 = new Account("VIN001", LocalDate.now(),  5000.20);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1),7500.35);
			Account account3 = new Account("VIN003", LocalDate.now().plusDays(2),500000.50);
			Account account4 = new Account("VIN004", LocalDate.now().plusDays(3),1050000.80);
			
			clientRepository.save(client1);
			client1.addAccount(account1);
			client1.addAccount(account2);
			accountRepository.save(account1);
			accountRepository.save(account2);
			clientRepository.save(client2);
			client2.addAccount(account3);
			client2.addAccount(account4);
			accountRepository.save(account3);
			accountRepository.save(account4);



			Transaction transaction1 = new Transaction(-2000.0,"Car fee",LocalDateTime.now(),TransactionType.DEBIT);
			Transaction transaction2 = new Transaction(1000.0,"Party last night",LocalDateTime.now(), TransactionType.CREDIT);
			Transaction transaction3 = new Transaction(-50000.0,"Debit Automatic",LocalDateTime.now(),TransactionType.DEBIT);
			Transaction transaction4 = new Transaction(75000.0, "Salary work", LocalDateTime.now(), TransactionType.CREDIT);

			account1.addTransactions(transaction1);
			account1.addTransactions(transaction2);
			account1.addTransactions(transaction3);
			account1.addTransactions(transaction4);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);


			Transaction transaction5 = new Transaction(-500.0,"Car fee",LocalDateTime.now(),TransactionType.DEBIT);
			Transaction transaction6 = new Transaction(1000.0,"super market",LocalDateTime.now(), TransactionType.CREDIT);
			Transaction transaction7 = new Transaction(-500.0,"retail purchase",LocalDateTime.now(),TransactionType.DEBIT);
			Transaction transaction8 = new Transaction(7500.0, "Salary work", LocalDateTime.now(), TransactionType.CREDIT);


			account2.addTransactions(transaction5);
			account2.addTransactions(transaction6);
			account2.addTransactions(transaction7);
			account2.addTransactions(transaction8);

			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);


			Transaction transaction9 = new Transaction(-1500.0,"Sundy",LocalDateTime.now(),TransactionType.DEBIT);
			Transaction transaction10 =new Transaction(1000.0,"super market",LocalDateTime.now(), TransactionType.CREDIT);
			Transaction transaction11= new Transaction(-5500.0,"Airplane",LocalDateTime.now(),TransactionType.DEBIT);
			Transaction transaction12= new Transaction(10000.0, "debit", LocalDateTime.now(), TransactionType.CREDIT);


			account3.addTransactions(transaction9);
			account3.addTransactions(transaction10);
			account3.addTransactions(transaction11);
			account3.addTransactions(transaction12);

			transactionRepository.save(transaction9);
			transactionRepository.save(transaction10);
			transactionRepository.save(transaction11);
			transactionRepository.save(transaction12);


			Transaction transaction13 = new Transaction(-2500.0,"Sundy",LocalDateTime.now(),TransactionType.DEBIT);
			Transaction transaction14 = new Transaction(10000.0,"super market",LocalDateTime.now(), TransactionType.CREDIT);
			Transaction transaction15 = new Transaction(-55000.0,"Airplane",LocalDateTime.now(),TransactionType.DEBIT);
			Transaction transaction16 = new Transaction(150000.0, "debit", LocalDateTime.now(), TransactionType.CREDIT);


			account4.addTransactions(transaction13);
			account4.addTransactions(transaction14);
			account4.addTransactions(transaction15);
			account4.addTransactions(transaction16);

			transactionRepository.save(transaction13);
			transactionRepository.save(transaction14);
			transactionRepository.save(transaction15);
			transactionRepository.save(transaction16);

		};

	}
}