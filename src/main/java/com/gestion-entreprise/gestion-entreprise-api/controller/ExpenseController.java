package com.example.springbootoauthjwt.controller;


import java.time.LocalDate;
import java.util.List;

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
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Expense;
import com.example.springbootoauthjwt.model.User;
import com.example.springbootoauthjwt.service.CompanyService;
import com.example.springbootoauthjwt.service.ExpenseServiceIMPL;
import com.example.springbootoauthjwt.service.UserService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@RestController
@CrossOrigin("*")
public class ExpenseController {
    @Autowired
    private ExpenseServiceIMPL expenseService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtDecoder jwtDecoder;
 

    @PostMapping("/create-expense/{companyId}")
    public ResponseEntity<Expense> registerExpenseHandler(@RequestHeader("Authorization") String token,@RequestBody  Expense request ,@PathVariable Long companyId) throws UserException, CompanyException, RoleException{

        Jwt jwt = jwtDecoder.decode(token.substring(7));

        User user = userService.findUserByEmail(jwt.getSubject().toString());

          Company optcompany = companyService.findByCompanyId(companyId);
           Expense expense=expenseService.registerExpense(user,request, optcompany);
      
        return new ResponseEntity<Expense>(expense, HttpStatus.OK);
    }
    
    @PutMapping("/update-expense/{companyId}/{expenseId}")
    public ResponseEntity<Expense> updateExpenseHandler(@RequestHeader("Authorization") String token, @RequestBody Expense newExpense,@PathVariable Long expenseId,
            @PathVariable Long companyId) throws ExpenseException, CompanyException, UserException, RoleException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User user = userRepository.findByEmail(jwt.getSubject().toString()).get();
        
        Expense oldExpense=expenseService.findExpenseById(expenseId);
        Company rCompany=companyService.findByCompanyId(companyId);
       
        Expense expense = expenseService.updateExpense(user,rCompany,oldExpense, newExpense);
    
        return new ResponseEntity<Expense>(expense, HttpStatus.OK);
    }
    
    @DeleteMapping("/{companyId}/{expenseId}")
    public ResponseEntity<String> deleteExpenseHandler(@RequestHeader("Authorization") String token,@PathVariable Long expenseId,
            @PathVariable Long companyId)
            throws RoleException, CustomerException, CompanyException, UserException, ExpenseException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User user = userRepository.findByEmail(jwt.getSubject().toString()).get();

        Expense expense = expenseService.findExpenseById(expenseId);
        Company rCompany = companyService.findByCompanyId(companyId);

        String message = expenseService.removeExpense(user, rCompany, expense);

        return new ResponseEntity<String>(message, HttpStatus.OK);
    }
    
    @GetMapping("/expenses/{companyId}")
    public ResponseEntity<List<Expense>> AllExpensesInCompanyHandler(@RequestHeader("Authorization") String token,
            @PathVariable Long companyId)
            throws RoleException, CustomerException, CompanyException, UserException, ExpenseException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User user = userRepository.findByEmail(jwt.getSubject().toString()).get();
       // System.out.println("companyName "+ company.getCompanyName());
        Company rCompany = companyService.findByCompanyId(companyId);

        List<Expense> expenses = expenseService.findAllExpensesInCompany(user, rCompany);

        return new ResponseEntity<List<Expense>>(expenses, HttpStatus.OK);
    }
 
    
    @PostMapping("/expenses/{companyId}")
    public ResponseEntity<List<Expense>> AllExpensesInCompanyBetweenDateHandler(@RequestHeader("Authorization") String token,
            @PathVariable Long companyId,@RequestBody Reqdate  request)
            throws RoleException, CustomerException, CompanyException, UserException, ExpenseException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User user = userRepository.findByEmail(jwt.getSubject().toString()).get();
        // System.out.println("companyName "+ company.getCompanyName());
        Company rCompany = companyService.findByCompanyId(companyId);

        List<Expense> expenses = expenseService.findAllExpensesByDate(user, rCompany, request.getDate1(),request.getDate2());

        return new ResponseEntity<List<Expense>>(expenses, HttpStatus.OK);
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Reqdate{
        private LocalDate date1,date2;
    }
    
}
