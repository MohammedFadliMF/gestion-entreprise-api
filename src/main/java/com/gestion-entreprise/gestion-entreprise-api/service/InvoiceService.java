package com.example.springbootoauthjwt.service;


import java.time.LocalDate;
import java.util.List;

import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.CustomerException;
import com.example.springbootoauthjwt.exceptions.InvoiceException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.TaxException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Invoice;
import com.example.springbootoauthjwt.model.User;


public interface InvoiceService {

    public Invoice findInvoiceById(Long invoiceId)throws InvoiceException;

    public Invoice createInvoice(User user,
                    Invoice invoice, Company company)throws CompanyException, UserException, RoleException ,CustomerException, TaxException ;
    
    public Invoice updateInvoice(User user,Company company,Invoice oldInvoice, Invoice newInvoice) 
                   throws InvoiceException, UserException, RoleException, CompanyException;
            
    public String removeInvoice( User user,Company company,  Invoice invoice)
                    throws InvoiceException, UserException, RoleException, CompanyException;

    public List<Invoice> findAll() ;
    
    public List<Invoice> findAllInvoicesInCompany(User user,Company company)
                    throws UserException, RoleException, CompanyException, InvoiceException;
    public List<Invoice> findAllInvoicesByDate(User user, Company company, LocalDate date1, LocalDate date2) throws CompanyException, InvoiceException, UserException, RoleException ;

    
}
