package com.example.springbootoauthjwt.dao;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
       
    Expense findByExpenceId(Long expenceId);

    @Query("SELECT e FROM Expense e WHERE e.date BETWEEN :startDate AND :endDate")
    List<Expense> findByDateBetween( LocalDateTime startDate, LocalDateTime endDate);

    //  @Query("SELECT e FROM Expense e WHERE e.company.companyId = :companyId AND e.date BETWEEN :startDate AND :endDate")
    // List<Expense> findExpensesByCompanyAndDateRange(@Param("companyId") Long companyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
   

}
