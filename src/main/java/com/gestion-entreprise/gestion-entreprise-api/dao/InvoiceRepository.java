package com.example.springbootoauthjwt.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.springbootoauthjwt.model.Customer;
import com.example.springbootoauthjwt.model.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
       
    public Optional<Invoice>  findByInvoiceId(Long invoiceId);
    

    public List <Invoice>findAll();
    
    List<Invoice> findAllByDatefBetween(LocalDate startDate, LocalDate endDate);

    // @Query("SELECT u From User u Where u.id IN :users")
    // public List<User> findAllUsersByUsersIds(@Param("users") List<Long> userIds);

    // @Query("SELECT DISTINCT u From User u Where u.username LIKE %:query% OR u.email LIKE %:query%")
    // public List<User> findByQuery(@Param("query") String query);

}
