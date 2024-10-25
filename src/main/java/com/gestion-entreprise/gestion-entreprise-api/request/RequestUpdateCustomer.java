package com.example.springbootoauthjwt.request;

import com.example.springbootoauthjwt.model.Customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class RequestUpdateCustomer {
    private Customer oldCustomer;
    private Customer newCustomer;
}
