package com.example.springbootoauthjwt.service;


import java.util.List;
import java.util.Set;

import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.TaxException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Invoice;
import com.example.springbootoauthjwt.model.Tax;
import com.example.springbootoauthjwt.model.User;


public interface TaxService {

    public Tax findTaxById(Long taxId)throws TaxException;

    public Tax createTax(User user,
                    Tax tax, Company company)throws CompanyException, UserException, RoleException, TaxException;
    
    public Tax updateTax(User user,Company company, Tax oldTax, Tax newTax) 
            throws UserException, TaxException, RoleException, CompanyException;
            
    public String removeTax( User user,Company company,  Tax tax) throws CompanyException, UserException, RoleException,
            TaxException;

    public List<Tax> findAll() ;
    
    public Set<Tax> findAllTaxInCompany(User user,Company company)
            throws UserException, RoleException, CompanyException, TaxException
                    ;
    
}
