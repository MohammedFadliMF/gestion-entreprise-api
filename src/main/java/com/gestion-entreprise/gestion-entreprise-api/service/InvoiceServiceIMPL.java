package com.example.springbootoauthjwt.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springbootoauthjwt.controller.InvoiceController.TransactionSummary;
import com.example.springbootoauthjwt.dao.CompanyRepository;
import com.example.springbootoauthjwt.dao.InvoiceRepository;
import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.CustomerException;
import com.example.springbootoauthjwt.exceptions.InvoiceException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.TaxException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Customer;
import com.example.springbootoauthjwt.model.Invoice;
import com.example.springbootoauthjwt.model.Tax;
import com.example.springbootoauthjwt.model.User;

import lombok.AllArgsConstructor;

@Service
public class InvoiceServiceIMPL  implements InvoiceService{
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TaxService taxService;

    @Override
    public Invoice createInvoice(User user, Invoice invoice, Company company) throws CompanyException, UserException, RoleException, CustomerException, TaxException  {
      
        Company optCompany=companyService.findByCompanyId(company.getCompanyId());
         if (companyService.getAllUsers(optCompany).contains(user)) {
          
            Invoice savedInvoice =new Invoice();
           
            Customer customer=customerService.findByCustomerId(invoice.getCustomer().getCustomerId());
            Tax tax=taxService.findTaxById(invoice.getTax().getTaxId());
          
            savedInvoice.setCustomer(customer);
            savedInvoice.setCreateAt(LocalDateTime.now());           
            savedInvoice.setLifecycle(invoice.getLifecycle());
            savedInvoice.setItems(invoice.getItems());
            savedInvoice.setDatef(invoice.getDatef());
            savedInvoice.setTotal_amount(invoice.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum()-(tax.getPercent()*invoice.getItems().stream()
                            .mapToDouble(item -> item.getQuantity() * item.getPrice())
                            .sum())/100);
            savedInvoice.setTax(tax);

            Invoice ss=invoiceRepository.save(savedInvoice);

            optCompany.getInvoices().add(ss);
            companyRepository.save(optCompany);
       return savedInvoice;
        }
         throw new UserException("you are not allowed !");
        } 
     
    

    @Override
    public Invoice updateInvoice(User user,Company company,Invoice oldInvoice, 
            Invoice newInvoice) throws UserException, RoleException, CompanyException, InvoiceException {
        // System.out.println("oldCustomer  "+oldCustomer.getEmail() );
        // System.out.println("newCustomer  " + newCustomer.getEmail());

        // if (!oldInvoice.getCompany().equals(newInvoice.getCompany() ) && !oldInvoice.getCustomer()
        //         .equals(newInvoice.getCustomer())) {
        //     throw new InvoiceException("Not The Same invoice");
        // }
        // System.out.println("company.getCustomers()  " + company.getCustomers());

        if (company.getInvoices().contains(oldInvoice) && companyService.getAllUsers(company).contains(user)) {
            
            oldInvoice.setCustomer(newInvoice.getCustomer()!=null ? newInvoice.getCustomer():oldInvoice.getCustomer());
            oldInvoice.setItems(newInvoice.getItems().isEmpty() ? oldInvoice.getItems():newInvoice.getItems());
            oldInvoice.setLifecycle(newInvoice.getLifecycle()!=null ? newInvoice.getLifecycle():oldInvoice.getLifecycle());
            oldInvoice.setTax(newInvoice.getTax() !=null ? newInvoice.getTax():oldInvoice.getTax());
           System.out.println("**********************************************");
           System.out.println(newInvoice.getTax().getName());
            Invoice  updatedInvoice = invoiceRepository.save(oldInvoice);
            if (updatedInvoice != null) {
                // System.out.println("*****************************************************");
                // System.out.println(updatedInvoice.getInvoiceId());
                return updatedInvoice;
            } else {
                throw new InvoiceException("Failed To update Invoice");
            }
        }

        throw new UserException("you are not allowed !");
    }

    
        @Override
        public String removeInvoice(User user,Company company, Invoice invoice) throws InvoiceException, UserException, RoleException, CompanyException
                {
             if (!company.getInvoices().contains(invoice)) {
                throw new InvoiceException("Invoice Not Found");
            }
            
            if ( companyService.getAllUsers(company).contains(user)) {
                company.getInvoices().remove(invoice);
                companyRepository.save(company);
                
                invoiceRepository.deleteById(invoice.getInvoiceId());
    
               return "has been deleted invoice :";
        }
        throw new UserException("Not Allowed ");    
        }
            
    @Override
    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

   


    @Override
    public List<Invoice> findAllInvoicesInCompany(User user,Company company) throws UserException, RoleException, CompanyException, InvoiceException  {
         if ( companyService.getAllUsers(company).contains(user)) {
             List<Invoice> invoices=company.getInvoices();

             if (invoices.isEmpty()) {
                 throw new InvoiceException("is Empty");
             }
             return invoices;
         }
       throw new UserException("Not Allowed");
    }



    @Override
    public Invoice findInvoiceById(Long invoiceId) throws InvoiceException {
     Optional<Invoice> invoice = invoiceRepository.findById(invoiceId);
        if (invoice.isPresent()) {
            return invoice.get();
        }
        throw new InvoiceException("Invoice Not Exist With Id: " + invoiceId);    }

@Override
public List<Invoice> findAllInvoicesByDate(User user, Company company, LocalDate date1, LocalDate date2) throws CompanyException, InvoiceException, UserException, RoleException {

      if (companyService.getAllUsers(company).contains(user)) {
          if (date1.isAfter(date2) || date1.isEqual(date2)) {
              throw new InvoiceException("\"From\" date must be before \"To\" date.");
          }

          List<Invoice> invoices = company.getInvoices().stream()
                  .filter(invoice -> (invoice.getDatef().isEqual(date1) || invoice.getDatef().isAfter(date1)) &&
                          (invoice.getDatef().isEqual(date2) || invoice.getDatef().isBefore(date2)))
                  .collect(Collectors.toList());

          if (invoices.isEmpty()) {
              throw new InvoiceException("No invoices found for the selected period.");
          }
          return invoices;
      }
      throw new UserException("You are not allowed.");
  }



  public List<Invoice> getInvoicesByDateRange(LocalDate startDate, LocalDate endDate) {
      return invoiceRepository.findAllByDatefBetween(startDate, endDate);
  }

  public List<TransactionSummary> getInvoiceSummaryByDateRange(Company company, String filter) {
     LocalDate startDate;
    LocalDate endDate;

    if ("mois".equalsIgnoreCase(filter)) {
        // For daily filter, set the date range to the current month
        startDate = YearMonth.now().atDay(1);
        endDate = startDate.plusMonths(1).minusDays(1);
    } else if ("année".equalsIgnoreCase(filter)) {
        // For monthly filter, set the date range to the current year
        startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        endDate = startDate.plusYears(1).minusDays(1);
    } else if ("year".equalsIgnoreCase(filter)) {
        // For yearly filter, set the date range to the current year
        startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        endDate = LocalDate.of(LocalDate.now().getYear(), 12, 31);
    } else {
        // Default to daily if the filter is not recognized
        startDate = YearMonth.now().atDay(1);
        endDate = startDate.plusMonths(1).minusDays(1);
    }  
    
    List<Invoice> invoices =company.getInvoices().stream()
              .filter(invoice -> !invoice.getDatef().isBefore(startDate) && !invoice.getDatef().isAfter(endDate))
              .collect(Collectors.toList());

      return invoices.stream()
              .collect(Collectors.groupingBy(invoice -> {
                  if ("mois".equalsIgnoreCase(filter)) {
                      return invoice.getDatef().toString();
                  } else if ("année".equalsIgnoreCase(filter)) {
                      return invoice.getDatef().getYear() + "-" + invoice.getDatef().getMonthValue();
                  } else if ("year".equalsIgnoreCase(filter)) {
                      return String.valueOf(invoice.getDatef().getYear());
                  } else {
                      return invoice.getDatef().toString();
                  }
              }, Collectors.summingDouble(Invoice::getTotal_amount)))
              .entrySet()
              .stream()
              .map(entry -> new TransactionSummary(entry.getKey(), entry.getValue()))
              .collect(Collectors.toList());
  }

 
}
