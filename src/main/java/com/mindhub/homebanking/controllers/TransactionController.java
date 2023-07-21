package com.mindhub.homebanking.controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import com.mindhub.homebanking.utils.UtilsTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
@RequestMapping("/api")
@RestController

public class TransactionController {
@Autowired
private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;


    @PostMapping("/accounts/transactions/pdf")
    public void PDF (Authentication authentication, @RequestParam long id, @RequestParam String startDate, @RequestParam String endDate, HttpServletResponse response) throws IOException, DocumentException, ParseException {
        Account account = accountService.findById(id);
        ClientDTO client = clientService.getClientAuthentication(authentication);

        if (account == null) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "The account doesn't exist");
            return;
        }

        if (client.getAccounts().stream().noneMatch(accountDTO -> accountDTO.getNumber().equals(account.getNumber()))) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "This account doesn't belong to you");
            return;
        }

        if (startDate.isBlank() || endDate.isBlank()) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Start date and end date can't be blank");
            return;
        }
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

        LocalDateTime startLocalDateTime = UtilsTransaction.dateToLocalDateTime(start);
        LocalDateTime endLocalDateTime = UtilsTransaction.dateToLocalDateTime(end).plusDays(1).minusSeconds(1);

        List<TransactionDTO> listTransaction = transactionService.findByAccountAndDateBetween(account, startLocalDateTime, endLocalDateTime);

        Document pdfTransactions = new Document(PageSize.A4);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String destiny = System.getProperty("user.home");
        PdfWriter.getInstance(pdfTransactions,new FileOutputStream(destiny + "/Desktop/transactions.pdf"));
        pdfTransactions.open();

        Image logo = Image.getInstance("C:\\Users\\doria\\Desktop\\homebanking\\src\\main\\resources\\static\\web\\asset\\logo.png");
        logo.scaleToFit(100,100);
        pdfTransactions.add(logo);

        Font title = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
        Paragraph paragraph = new Paragraph("Hello " + client.getFirstName() + " " + client.getLastName(), title);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        paragraph.setSpacingAfter(22);
        paragraph.setSpacingBefore(22);
        pdfTransactions.add(paragraph);

        Font subTitle = new Font(Font.FontFamily.HELVETICA, 20);
        Paragraph subParagraph = new Paragraph("Here I leave the transactions made of the day " + startDate + " to " + endDate + " from your account " + account.getNumber(), subTitle);
        subParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        subParagraph.setSpacingAfter(22);
        subParagraph.setSpacingBefore(22);
        pdfTransactions.add(subParagraph);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);

        PdfPCell amount = new PdfPCell(new Paragraph("Amount"));
        amount.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(amount);

        PdfPCell description = new PdfPCell(new Paragraph("Description"));
        description.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(description);

        PdfPCell date = new PdfPCell(new Paragraph("Date"));
        date.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(date);

        PdfPCell type = new PdfPCell(new Paragraph("Type"));
        type.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(type);

        PdfPCell time = new PdfPCell(new Paragraph("Time"));
        time.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(time);

        PdfPCell balanceCurrent = new PdfPCell(new Paragraph("Current Balance"));
        balanceCurrent.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(balanceCurrent);

        for (TransactionDTO transactionDTO: listTransaction){
            String dateFormat = UtilsTransaction.getStringDateFromLocalDateTime(transactionDTO.getDate());
            String hour = UtilsTransaction.getStringHourFromLocalDateTime(transactionDTO.getDate());

            PdfPCell transactionAmount = new PdfPCell(new Paragraph(transactionDTO.getType().name().equals("DEBIT")? "U$D" + (NumberFormat.getNumberInstance(Locale.US).format(transactionDTO.getAmount())) : "U$D " + NumberFormat.getNumberInstance(Locale.US).format(transactionDTO.getAmount())));
            transactionAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionAmount.setVerticalAlignment(Element.ALIGN_MIDDLE);
            transactionAmount.setFixedHeight(40);
            table.addCell(transactionAmount);

            PdfPCell transactionDescription = new PdfPCell(new Paragraph(transactionDTO.getDescription()));
            transactionDescription.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionDescription.setVerticalAlignment(Element.ALIGN_MIDDLE);
            transactionDescription.setFixedHeight(40);
            table.addCell(transactionDescription);

            PdfPCell transactionDate = new PdfPCell(new Paragraph(dateFormat));
            transactionDate.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
            transactionDate.setFixedHeight(40);
            table.addCell(transactionDate);

            PdfPCell transactionType = new PdfPCell(new Paragraph(transactionDTO.getType().name()));
            transactionType.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionType.setVerticalAlignment(Element.ALIGN_MIDDLE);
            transactionType.setFixedHeight(40);
            table.addCell(transactionType);

            PdfPCell transactionTime = new PdfPCell(new Paragraph(hour));
            transactionTime.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionTime.setVerticalAlignment(Element.ALIGN_MIDDLE);
            transactionTime.setFixedHeight(40);
            table.addCell(transactionTime);

            PdfPCell currentBalance = new PdfPCell(new Paragraph("U$D " + NumberFormat.getNumberInstance(Locale.US).format(transactionDTO.getBalanceTotal())));
            currentBalance.setHorizontalAlignment(Element.ALIGN_CENTER);
            currentBalance.setVerticalAlignment(Element.ALIGN_MIDDLE);
            currentBalance.setFixedHeight(40);
            table.addCell(currentBalance);


        }
        response.setHeader("Content-Disposition", "attachment; filename=\"Transaction From " + startDate + " to " + endDate + ".pdf\"");
        response.setContentType("application/pdf");


        response.getOutputStream().flush();
        pdfTransactions.add(table);
        pdfTransactions.close();

    }

    @Transactional
    @PostMapping( "/transactions")

    public ResponseEntity<Object> newTransaction (Authentication authentication
            , @RequestParam double amount, @RequestParam String description
            , @RequestParam String accountOrigin, @RequestParam String accountDestiny ){

        Client client = clientService.findByEmail(authentication.getName());
        Account selectAccountOrigin = accountService.findByNumber(accountOrigin.toUpperCase());
        Account selectAccountDestiny= accountService.findByNumber(accountDestiny.toUpperCase());

        if (amount < 1){
            return new ResponseEntity<>("You  cannot send negative balance", HttpStatus.EXPECTATION_FAILED);
        }
        if (description.isBlank()){
            return new ResponseEntity<>("Please add a description", HttpStatus.FORBIDDEN);
        }
        if (selectAccountOrigin == null){
            return new ResponseEntity<>("This account does not exist", HttpStatus.FORBIDDEN);
        }
        if (selectAccountOrigin.equals(selectAccountDestiny)){
            return new ResponseEntity<>("You cannot send money to the same account", HttpStatus.FORBIDDEN);
        }
        if (selectAccountDestiny == null){
            return new ResponseEntity<>("This account does not exist", HttpStatus.FORBIDDEN);
        }
        if (selectAccountOrigin.getBalance() < amount){
            return new ResponseEntity<>("you don't have enough balance", HttpStatus.FORBIDDEN);
        }
        if (client.getAccounts().stream().filter(account -> account.getNumber().equalsIgnoreCase(accountOrigin)).collect(Collectors.toList()).size() == 0){
            return new ResponseEntity<>("This account is not yours", HttpStatus.FORBIDDEN);
        }


        Transaction transactionOrigin = new Transaction(Double.parseDouble("-" + amount), description, LocalDateTime.now(),TransactionType.DEBIT,true,selectAccountOrigin.getBalance() -amount);
        Transaction transactionDestin = new Transaction(amount, description, LocalDateTime.now(),TransactionType.CREDIT,true,selectAccountDestiny.getBalance() +amount);


        selectAccountOrigin.setBalance(selectAccountOrigin.getBalance() - amount);
        selectAccountDestiny.setBalance(selectAccountDestiny.getBalance() + amount);

        selectAccountOrigin.addTransactions(transactionOrigin);
        selectAccountDestiny.addTransactions(transactionDestin);

        transactionService.saveTransaction(transactionOrigin);
        transactionService.saveTransaction(transactionDestin);

        return new ResponseEntity<>("Transaction Sucess",HttpStatus.CREATED);
    }

}