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
import com.example.springbootoauthjwt.dao.ExpenseRepository;
import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.ExpenseException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Expense;
import com.example.springbootoauthjwt.model.User;


@Service
public class ExpenseServiceIMPL  implements ExpenseService{
    
    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    CompanyService companyService;
    @Autowired
    CompanyRepository companyRepository;
    @Override
    public Expense registerExpense(User user,Expense expense, Company company) throws UserException, RoleException, CompanyException {

        if (companyService.getAllUsers(company).contains(user)) {
            Expense savedExpense=new Expense();
            savedExpense.setAmount(expense.getAmount());
            savedExpense.setCategory(expense.getCategory());
            savedExpense.setNote(expense.getNote());
            savedExpense.setDate(expense.getDate());
            // savedExpense.setCreateAt(LocalDateTime.now());

            savedExpense=expenseRepository.save(savedExpense);
            company.getExpences().add(savedExpense);
            companyRepository.save(company);
            return savedExpense;
         }
         throw new UserException("you are not allowed !");
    }
    @Override
    public Expense updateExpense(User user, Company company, Expense oldExpense, Expense newExpense)
            throws ExpenseException, UserException, CompanyException, RoleException {
        
        if (!company.getExpences().contains(oldExpense )) {
            throw new ExpenseException("Expense Not Found !");
        }
        if (companyService.getAllUsers(company).contains(user)) {
            Expense updatedExpense = new Expense();
            updatedExpense.setAmount(newExpense.getAmount()!=null ? newExpense.getAmount():oldExpense.getAmount());
            updatedExpense.setCategory(newExpense.getCategory()!=null ?newExpense.getCategory() : oldExpense.getCategory());
            updatedExpense.setNote(newExpense.getNote()!=null ?newExpense.getNote() : oldExpense.getNote());
            updatedExpense = expenseRepository.save(updatedExpense);
            return updatedExpense;
        }
        throw new UserException("you are not allowed !");

    }
    @Override
    public String removeExpense(User user, Company company, Expense expense)
            throws ExpenseException, UserException, CompanyException, RoleException {
        if (!company.getExpences().contains(expense)) {
            throw new ExpenseException("Expense Not Found");
        }

        if (companyService.getAllUsers(company).contains(user)) {
            company.getExpences().remove(expense);
            companyRepository.save(company);

            expenseRepository.deleteById(expense.getExpenceId());

            return "has been deleted expense :" + expense.getExpenceId();
        }
        throw new UserException("you are not allowed ");
    }
    @Override
    public List<Expense> findAll() {
       return expenseRepository.findAll();
    }
    @Override
    public List<Expense> findAllExpensesInCompany(User user, Company company)
            throws CompanyException, ExpenseException, UserException, RoleException {
        if (companyService.getAllUsers(company).contains(user)) {
            List<Expense> expenses = company.getExpences();

            if (expenses.isEmpty()) {
                throw new ExpenseException("Vide");
            }
            return expenses;
        }
        throw new UserException("you are not allowed ");
    }
    @Override
    public Expense findExpenseById(Long id) throws ExpenseException {
        Optional<Expense> expense = expenseRepository.findById(id);
        if (expense.isPresent()) {
            return expense.get();
        }
        throw new ExpenseException("Expense Not Exist With Id: " + id);
  
  }
  
  @Override
  public List<Expense> findAllExpensesByDate(User user, Company company, LocalDate date1, LocalDate date2)
          throws CompanyException, ExpenseException, UserException, RoleException {

      if (companyService.getAllUsers(company).contains(user)) {
          if (date1.isAfter(date2) || date1.isEqual(date2)) {
              throw new ExpenseException("\"From\" date must be before \"To\" date.");
          }

          List<Expense> expenses = company.getExpences().stream()
                  .filter(ex -> (ex.getDate().isEqual(date1) || ex.getDate().isAfter(date1)) &&
                          (ex.getDate().isEqual(date2) || ex.getDate().isBefore(date2)))
                  .collect(Collectors.toList());

          if (expenses.isEmpty()) {
              throw new ExpenseException("No expenses found for the selected period.");
          }
          return expenses;
      }
      throw new UserException("You are not allowed.");
  }


  public List<TransactionSummary> getExpenseSummaryByDateRange(Company company, String filter) {
    LocalDate startDate;
    LocalDate endDate;

    if ("mois".equalsIgnoreCase(filter)) {
        // For monthly filter, set the date range to the current month
        startDate = YearMonth.now().atDay(1);
        endDate = startDate.plusMonths(1).minusDays(1);
    } else if ("année".equalsIgnoreCase(filter)) {
        // For yearly filter, set the date range to the current year
        startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        endDate = startDate.plusYears(1).minusDays(1);
    } else if ("year".equalsIgnoreCase(filter)) {
        // For yearly filter, set the date range to the current year
        startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        endDate = LocalDate.of(LocalDate.now().getYear(), 12, 31);
    } else {
        // Default to monthly if the filter is not recognized
        startDate = YearMonth.now().atDay(1);
        endDate = startDate.plusMonths(1).minusDays(1);
    }

    List<Expense> expenses = company.getExpences().stream()
            .filter(expense -> !expense.getDate().isBefore(startDate) && !expense.getDate().isAfter(endDate))
            .collect(Collectors.toList());

    return expenses.stream()
            .collect(Collectors.groupingBy(expense -> {
                if ("mois".equalsIgnoreCase(filter)) {
                    return expense.getDate().toString();
                } else if ("année".equalsIgnoreCase(filter)) {
                    return expense.getDate().getYear() + "-" + expense.getDate().getMonthValue();
                } else if ("year".equalsIgnoreCase(filter)) {
                    return String.valueOf(expense.getDate().getYear());
                } else {
                    return expense.getDate().toString();
                }
            }, Collectors.summingDouble(Expense::getAmount)))
            .entrySet()
            .stream()
            .map(entry -> new TransactionSummary(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
}



}
