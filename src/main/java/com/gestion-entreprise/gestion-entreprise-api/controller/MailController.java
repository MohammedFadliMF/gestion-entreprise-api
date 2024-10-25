package com.example.springbootoauthjwt.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbootoauthjwt.model.MailStructure;
import com.example.springbootoauthjwt.service.CompanyService;
import com.example.springbootoauthjwt.service.InvoiceService;
import com.example.springbootoauthjwt.service.MailService;
import com.example.springbootoauthjwt.dao.InvoiceRepository;
import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.InvoiceException;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Invoice;
import com.example.springbootoauthjwt.model.InvoiceGenarator;
import com.example.springbootoauthjwt.model.InvoiceItem;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/mail")
@CrossOrigin("*")
public class MailController {
    @Autowired
    private MailService mailService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private InvoiceRepository invoiceRepository;

    // @PostMapping("/send")
    // public String sendMail(@RequestBody MailStructure mailStructure) {
    //     mailService.sendMail( mailStructure);
    //     return "Successfully";
    // }
    
    @GetMapping("/generate-invoice")
    public String generateInvoice() {
        InvoiceGenarator invoiceGenerator = new InvoiceGenarator();
        try {
            String customerName="customerName";
            String productName = "productName";
            String price = "price";

            invoiceGenerator.generateInvoice(customerName, productName, price);
            return "Invoice generated successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error generating invoice";
        }
    }

    
    @GetMapping("/generate-i/{invoiceId}/{companyId}")
    public ResponseEntity<InputStreamResource> generateInvoice(@RequestHeader("Authorization") String token,@PathVariable Long invoiceId, @PathVariable Long companyId) throws IOException,InvoiceException, CompanyException {

        Company company=companyService.findByCompanyId(companyId);
        Invoice invoice=invoiceService.findInvoiceById(invoiceId);
        // Set<InvoiceItem> itemss=invoice.getItems();
        // String i=String.valueOf(invoice.getInvoiceId());
        
        String invoiceNumber=String.valueOf(invoice.getInvoiceId());
        
        String invoiceDate=String.valueOf(invoice.getCreateAt());
       
        String customerName=String.valueOf(invoice.getCustomer().getCustomerName());
       
        String customerAddress= String.valueOf(invoice.getCustomer().getBillingAddress());

        String totalAmount= String.valueOf(invoice.getTotal_amount());
        Set<InvoiceItem> items = invoice.getItems();
        String companyName=company.getCompanyName();
        String companyEmail=company.getCompanyEmail();
         
         byte[] pdfBytes = mailService.generateInvoice(companyName,companyEmail,
                invoiceNumber, invoiceDate, customerName, customerAddress, 
                 items, totalAmount);
        ByteArrayInputStream bis = new ByteArrayInputStream(pdfBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoice.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/send-invoice/{invoiceId}/{companyId}")
    public ResponseEntity<String> sendInvoice(@RequestHeader("Authorization") String token,@PathVariable Long invoiceId, @PathVariable Long companyId) throws IOException,InvoiceException, CompanyException {
        Company company = companyService.findByCompanyId(companyId);
        Invoice invoice = invoiceService.findInvoiceById(invoiceId);
        // Set<InvoiceItem> itemss=invoice.getItems();
        // String i=String.valueOf(invoice.getInvoiceId());

        String invoiceNumber = String.valueOf(invoice.getInvoiceId());

        String invoiceDate = String.valueOf(invoice.getCreateAt());

        String customerName = String.valueOf(invoice.getCustomer().getCustomerName());

        String customerAddress = String.valueOf(invoice.getCustomer().getBillingAddress());

        String totalAmount = String.valueOf(invoice.getTotal_amount());
        Set<InvoiceItem> items = invoice.getItems();
        String companyName = company.getCompanyName();
        String companyEmail = company.getCompanyEmail();

        byte[] pdfBytes = mailService.generateInvoice(companyName, companyEmail,
                invoiceNumber, invoiceDate, customerName, customerAddress,
                items, totalAmount);
        MailStructure mailStructure=new MailStructure();
        mailStructure.setMessage(companyEmail);
        mailStructure.setSender(companyName);
        mailStructure.setSubject(companyEmail);
        mailStructure.setTo(invoice.getCustomer().getEmail());
        mailService.sendMail(mailStructure, pdfBytes);
        invoice.setLifecycle("SENT");
        invoiceRepository.save(invoice);

        return ResponseEntity.ok("Facture envoyée avec succès");
    }

}
