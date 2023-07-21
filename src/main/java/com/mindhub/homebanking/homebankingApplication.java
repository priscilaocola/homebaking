package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;


@SpringBootApplication
public class homebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(homebankingApplication.class, args);
	}
//	LocalDateTime day = LocalDateTime.now();
//	List<Integer> mortgage = List.of(12, 24, 36, 48, 60);
//	List<Integer> personal = List.of(6, 12, 24);
//	List<Integer> automotive = List.of(6, 12, 24, 36);
@Autowired
private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData (ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return args -> {

			Client admin = new Client("admin", "admin", "admin@gmail.com", passwordEncoder.encode("adm1234"));
			Client client1= new Client("Melba","Morel","melba@mindhub.com", passwordEncoder.encode("melba1234"));
			Client client2= new Client("Dorian", "Ocola","dorian@gmail.com",passwordEncoder.encode("dorian1234"));

			Account account1 = new Account("VIN-001", LocalDate.now(),  5000.20,true, AccountType.CURRENT);
			Account account2 = new Account("VIN-002", LocalDate.now().plusDays(1),7500.35,true,AccountType.SAVINGS);
			Account account3 = new Account("VIN-003", LocalDate.now().plusDays(2),500000.50,true, AccountType.CURRENT);
			Account account4 = new Account("VIN-004", LocalDate.now().plusDays(3),1050000.80,true, AccountType.SAVINGS);


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
			clientRepository.save(admin);

			Transaction transaction1 = new Transaction(-2000.0,"Car fee",LocalDateTime.now(), TransactionType.DEBIT,true,account1.getBalance());
			Transaction transaction2 = new Transaction(1000.0,"Party last night",LocalDateTime.now(), TransactionType.CREDIT,true,account1.getBalance());
			Transaction transaction3 = new Transaction(-50000.0,"Debit Automatic",LocalDateTime.now(),TransactionType.DEBIT,true,account1.getBalance());
			Transaction transaction4 = new Transaction(75000.0, "Salary work", LocalDateTime.now(), TransactionType.CREDIT,true,account1.getBalance());


			account1.addTransactions(transaction1);
			account1.addTransactions(transaction2);
			account1.addTransactions(transaction3);
			account1.addTransactions(transaction4);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);



			Transaction transaction5 = new Transaction(-500.0,"Car fee",LocalDateTime.now(),TransactionType.DEBIT,true,account2.getBalance());
			Transaction transaction6 = new Transaction(1000.0,"super market",LocalDateTime.now(), TransactionType.CREDIT,true,account2.getBalance());
			Transaction transaction7 = new Transaction(-500.0,"retail purchase",LocalDateTime.now(),TransactionType.DEBIT,true,account2.getBalance());
			Transaction transaction8 = new Transaction(7500.0, "Salary work", LocalDateTime.now(), TransactionType.CREDIT,true,account2.getBalance());

			account2.addTransactions(transaction5);
			account2.addTransactions(transaction6);
			account2.addTransactions(transaction7);
			account2.addTransactions(transaction8);

			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);

			Transaction transaction9 = new Transaction(-1500.0,"Sundy",LocalDateTime.now(),TransactionType.DEBIT,true,account3.getBalance());
			Transaction transaction10 =new Transaction(1000.0,"super market",LocalDateTime.now(), TransactionType.CREDIT,true,account3.getBalance());


			account3.addTransactions(transaction9);
			account3.addTransactions(transaction10);

			transactionRepository.save(transaction9);
			transactionRepository.save(transaction10);



			Transaction transaction13 = new Transaction(-2500.0,"Sundy",LocalDateTime.now(),TransactionType.DEBIT,true,account4.getBalance());
			Transaction transaction14 = new Transaction(10000.0,"super market",LocalDateTime.now(), TransactionType.CREDIT,true,account4.getBalance());
			Transaction transaction15 = new Transaction(-55000.0,"Airplane",LocalDateTime.now(),TransactionType.DEBIT,true,account4.getBalance());
			Transaction transaction16 = new Transaction(150000.0, "debit", LocalDateTime.now(), TransactionType.CREDIT,true,account4.getBalance());

			account4.addTransactions(transaction13);
			account4.addTransactions(transaction14);
			account4.addTransactions(transaction15);
			account4.addTransactions(transaction16);

			transactionRepository.save(transaction13);
			transactionRepository.save(transaction14);
			transactionRepository.save(transaction15);
			transactionRepository.save(transaction16);


			Loan loan1 = new Loan("MORTGAGE", 500000.0, Arrays.asList(12,24,36,48,60),10);
			Loan loan2 = new Loan("PERSON", 100000.0, Arrays.asList(6,12,24),20);
			Loan loan3 = new Loan("AUTOMOTIVE", 300000.0, Arrays.asList(6,12,24,36),30);
			ClientLoan clientLoan1 = new ClientLoan(400000.0, 60,30 , 300000.0);
			ClientLoan clientLoan2 = new ClientLoan(50000.0, 12,12,50000.0 );
			ClientLoan clientLoan3 = new ClientLoan(100000.0, 24,12,12200.0 );
			ClientLoan clientLoan4 = new ClientLoan(200000.0, 36,12,15000.0);

			client1.addClientLoan(clientLoan1);
			loan1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			loan2.addClientLoan(clientLoan2);
			client2.addClientLoan(clientLoan3);
			loan2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);
			loan3.addClientLoan(clientLoan4);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			clientLoanRepository.save (clientLoan1);
			clientLoanRepository.save (clientLoan2);
			clientLoanRepository.save (clientLoan3);
			clientLoanRepository.save (clientLoan4);


			Card card1 = new Card( CardType.DEBIT, CardColor.GOLD, "1234 4678 9876 5432", LocalDate.now(), LocalDate.now().plusYears(5), 125,client1.getFirstName() + " " + client1.getLastName(),true);
			Card card2 = new Card( CardType.CREDIT, CardColor.TITANIUM,"3445 4454 7588 9234", LocalDate.now(), LocalDate.now().plusYears(5), 753,client1.getFirstName()+ " " + client1.getLastName(),true);
			Card card3 = new Card( CardType.CREDIT, CardColor.SILVER, "4547 6534 4391 2795", LocalDate.now(), LocalDate.now().plusYears(5) ,394,client2.getFirstName()+ " " + client2.getLastName(),true);
			Card card4 = new Card( CardType.CREDIT, CardColor.TITANIUM, "5390 4587 3255 0050", LocalDate.now(), LocalDate.now().plusYears(5) ,250,client2.getFirstName()+ " " + client2.getLastName(),false);
			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);
			client2.addCard(card4);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
			cardRepository.save(card4);

		};

	}
}
