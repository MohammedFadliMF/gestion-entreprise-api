package com.example.springbootoauthjwt.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import jakarta.persistence.OneToOne;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @NonNull 
    private String customerName;
    @NonNull
    private String primaryContactname;
    @NonNull 
    private String email;
    @NonNull
    private String phone;
    @NonNull
    private String BillingAddress;
    @NonNull
    private String ShippingAddress;
    

    
}
