package com.example.springbootoauthjwt.service;

import java.time.LocalDate;
import java.util.List;

import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.CustomerException;
import com.example.springbootoauthjwt.exceptions.ExpenseException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Customer;
import com.example.springbootoauthjwt.model.Expense;
import com.example.springbootoauthjwt.model.User;


public interface ExpenseService {
    public  Expense registerExpense(User user,Expense expense, Company company)  throws UserException, RoleException, CompanyException ;
    
       public Expense updateExpense(User userId,Company company,Expense oldExpense, 
               Expense newExpense) 
            throws ExpenseException, UserException, CompanyException, RoleException;
            
    public String removeExpense( User user,Company company,  
            Expense expense)
            throws ExpenseException, UserException, CompanyException, RoleException;

    public List<Expense> findAll() ;
    
    public Expense findExpenseById(Long id) throws ExpenseException;
    
    public List<Expense> findAllExpensesInCompany(User user,Company company)
                    throws CompanyException, ExpenseException, UserException, RoleException;
    
    public List<Expense> findAllExpensesByDate(User user, Company company,LocalDate date1,LocalDate date2)
            throws CompanyException, ExpenseException, UserException, RoleException;


}
