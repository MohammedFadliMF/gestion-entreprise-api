package com.example.springbootoauthjwt.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.example.springbootoauthjwt.dtos.ItemDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
public class Company {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long companyId;

    @NonNull
    @Column(unique = true)
    private String companyEmail;
    @NonNull 
    private String companyAdress;
    @NonNull
    private String companyName;

    @OneToMany
    private List<Customer> customers=new ArrayList<Customer>();
    
    
    @Embedded
    @ElementCollection
    private Set<ItemDTO>items=new HashSet<ItemDTO>();
   
    @OneToMany()
    private List<Expense> expences=new ArrayList<Expense>();

    @OneToMany
    private List<Invoice> invoices=new ArrayList<Invoice>();
   
    @OneToMany()
    private Set<Tax> taxes = new HashSet<Tax>();

}
