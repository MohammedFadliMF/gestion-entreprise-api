package com.example.springbootoauthjwt.service;


import java.util.List;

import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.CustomerException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Customer;
import com.example.springbootoauthjwt.model.User;


public interface CustomerService {

    public Customer createCustomer(User user,Customer customer, Company company) throws CompanyException, CustomerException,UserException,
            RoleException;
    
    public Customer updateCustomer(User userId,Company company,Customer oldCustomer, Customer newCustomer) 
            throws CustomerException, UserException, CompanyException, RoleException;
    
            public Customer findByCustomerId(Long customerId) throws CustomerException ;
            
    public String removeCustomer( User user,Company company,  Customer customer)
            throws CustomerException, UserException, CompanyException, RoleException;

    public List<Customer> findAll() ;
    
    public List<Customer> findAllCustomersInCompany(User user,Company company)
                    throws CompanyException, CustomerException, UserException, RoleException;
    
    public Customer findCustomerByEmail(String  email) throws CustomerException;

    
}
