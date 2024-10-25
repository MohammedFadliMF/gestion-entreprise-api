package com.example.springbootoauthjwt.request;

import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class RequestDeleteCustomer {
    private Company company;
    private Customer customer;
}
