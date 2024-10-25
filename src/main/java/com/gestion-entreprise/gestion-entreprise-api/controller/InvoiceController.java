package com.example.springbootoauthjwt.controller;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import com.example.springbootoauthjwt.dao.UserRepository;
import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.CustomerException;
import com.example.springbootoauthjwt.exceptions.ExpenseException;
import com.example.springbootoauthjwt.exceptions.InvoiceException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.TaxException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Expense;
import com.example.springbootoauthjwt.model.Invoice;
import com.example.springbootoauthjwt.model.User;
import com.example.springbootoauthjwt.service.CompanyService;
import com.example.springbootoauthjwt.service.ExpenseServiceIMPL;
import com.example.springbootoauthjwt.service.InvoiceService;
import com.example.springbootoauthjwt.service.InvoiceServiceIMPL;
import com.example.springbootoauthjwt.service.UserService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RestController
@RequestMapping("/invoice")
@CrossOrigin("*")
public class InvoiceController {
    @Autowired
    private InvoiceServiceIMPL invoiceService;
    @Autowired
    private ExpenseServiceIMPL expenseServiceIMPL;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtDecoder jwtDecoder;

    @PostMapping("/create-invoice/{companyId}")
    public ResponseEntity<Invoice> registerInvoiceHandler(@RequestHeader("Authorization") String token,
            @RequestBody Invoice invoice, @PathVariable Long companyId)
            throws CompanyException, UserException, RoleException, CustomerException, TaxException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));

        User user = userService.findUserByEmail(jwt.getSubject().toString());

        Company optcompany = companyService.findByCompanyId(companyId);
        // System.out.println("Tax ==> "+invoice.getTax());
        Invoice created_i = invoiceService.createInvoice(user, invoice, optcompany);

        return new ResponseEntity<Invoice>(created_i, HttpStatus.CREATED);
    }

    @PutMapping("/update/{invoiceId}/{companyId}")
    public ResponseEntity<Invoice> updateInvoiceHandler(@RequestHeader("Authorization") String token,
            @RequestBody Invoice newInvoice, @PathVariable Long invoiceId, @PathVariable Long companyId)
            throws RoleException, CustomerException, CompanyException, UserException, InvoiceException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User user = userRepository.findByEmail(jwt.getSubject().toString()).get();

        Invoice rInvoice = invoiceService.findInvoiceById(invoiceId);
        Company rCompany = companyService.findByCompanyId(companyId);

        Invoice invoice = invoiceService.updateInvoice(user, rCompany, rInvoice, newInvoice);

        return new ResponseEntity<Invoice>(invoice, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{invoiceId}/{companyId}")
    public ResponseEntity<String> deleteInvoiceHandler(@RequestHeader("Authorization") String token,
            @PathVariable Long invoiceId, @PathVariable Long companyId)
            throws RoleException, CustomerException, CompanyException, UserException, InvoiceException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User user = userRepository.findByEmail(jwt.getSubject().toString()).get();

        Invoice rInvoice = invoiceService.findInvoiceById(invoiceId);
        Company rCompany = companyService.findByCompanyId(companyId);

        String message = invoiceService.removeInvoice(user, rCompany, rInvoice);

        return new ResponseEntity<String>(message, HttpStatus.OK);
    }

    @GetMapping("/all/{companyId}")
    public ResponseEntity<List<Invoice>> AllInvoiceInCompanyHandler(@RequestHeader("Authorization") String token,
            @PathVariable Long companyId)
            throws RoleException, CustomerException, CompanyException, UserException, InvoiceException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User user = userRepository.findByEmail(jwt.getSubject().toString()).get();
        // System.out.println("companyName "+ company.getCompanyName());
        Company rCompany = companyService.findByCompanyId(companyId);

        List<Invoice> invoices = invoiceService.findAllInvoicesInCompany(user, rCompany);

        return new ResponseEntity<List<Invoice>>(invoices, HttpStatus.OK);
    }

    @GetMapping("/range-date/{companyId}")
    public ResponseEntity<List<Invoice>> AllInvoicesInCompanyBetweenDateHandler(
            @RequestHeader("Authorization") String token,
            @PathVariable Long companyId, @RequestParam LocalDate date1, @RequestParam LocalDate date2)
            throws RoleException, CustomerException, CompanyException, UserException, InvoiceException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User user = userRepository.findByEmail(jwt.getSubject().toString()).get();
        // System.out.println("companyName "+ company.getCompanyName());
        Company rCompany = companyService.findByCompanyId(companyId);

        List<Invoice> invoices = invoiceService.findAllInvoicesByDate(user, rCompany, date1, date2);
        return new ResponseEntity<List<Invoice>>(invoices, HttpStatus.OK);
    }

    @GetMapping("/summary-invoice/{companyId}")
    public Map<String, List<TransactionSummary>> getInvoiceSummary(
            @RequestHeader("Authorization") String token,@PathVariable Long companyId,
            @RequestParam("filter") String filter) throws CompanyException {
        Company company = companyService.findByCompanyId(companyId);
        List<TransactionSummary> invoiceSummaries = invoiceService.getInvoiceSummaryByDateRange(company,
                filter);
        // List<TransactionSummary> expenseSummaries = expenseServiceIMPL.getExpenseSummaryByDateRange(company,
        //         filter);

        Map<String, List<TransactionSummary>> summaryMap = new HashMap<>();
        summaryMap.put("invoices", invoiceSummaries);
        // summaryMap.put("expenses", expenseSummaries);

        return summaryMap;
    }
    
    @GetMapping("/summary-expense/{companyId}")
    public Map<String, List<TransactionSummary>> getExpenseSummary(
            @RequestHeader("Authorization") String token, @PathVariable Long companyId,
            @RequestParam("filter") String filter) throws CompanyException {
        Company company = companyService.findByCompanyId(companyId);
        // List<TransactionSummary> invoiceSummaries = invoiceService.getInvoiceSummaryByDateRange(company,
        //         filter);
        List<TransactionSummary> expenseSummaries = expenseServiceIMPL.getExpenseSummaryByDateRange(company,
                filter);

        Map<String, List<TransactionSummary>> summaryMap = new HashMap<>();
        // summaryMap.put("invoices", invoiceSummaries);
        summaryMap.put("expenses", expenseSummaries);

        return summaryMap;
    }
 
    private List<TransactionSummary> generateLabels(List<TransactionSummary> summaries, String filter) {
        // Initialize labels and map to hold the summaries
        List<String> labels = new ArrayList<>();
        Map<String, TransactionSummary> summaryMap = new HashMap<>();

        // Populate labels and map based on the filter
        if ("mois".equalsIgnoreCase(filter)) {
            // For monthly filter, add labels from 1 to 30/31 for each month
            for (int i = 1; i <= 31; i++) { // Assuming maximum 31 days
                labels.add(String.valueOf(i));
            }
        } else if ("année".equalsIgnoreCase(filter)) {
            // For yearly filter, add labels for each month of the year
            for (int i = 1; i <= 12; i++) { // 12 months
                labels.add(String.valueOf(i));
            }
        } else if ("year".equalsIgnoreCase(filter)) {
            // For yearly filter, add labels for each year
            int currentYear = Year.now().getValue();
            for (int i = currentYear - 5; i <= currentYear; i++) { // Last 5 years
                labels.add(String.valueOf(i));
            }
        }

        // Initialize summaries with 0 amount for each label
        for (String label : labels) {
            summaryMap.put(label, new TransactionSummary(label, 0));
        }

        // Update summaries with the actual amounts
        for (TransactionSummary summary : summaries) {
            summaryMap.put(summary.getDate(), summary);
        }

        // Return the summaries in the order of labels
        return labels.stream().map(summaryMap::get).collect(Collectors.toList());
    }
 
public static class TransactionSummary {
        private String date;
        private double totalAmount;

        public TransactionSummary(String date, double totalAmount) {
            this.date = date;
            this.totalAmount = totalAmount;
        }

        public String getDate() {
            return date;
        }

        public double getTotalAmount() {
            return totalAmount;
        }
        
        public void setDate(String date) {
             this.date=date;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestCreateInvoice {
        private Invoice invoice;
        private String companyName;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestUpdateInvoice {
        private Invoice oldInvoice;
        private Invoice newInvoice;
        private String companyName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class RequestDeleteInvoice {
        private Company company;
        private Invoice invoice;
    }
}
