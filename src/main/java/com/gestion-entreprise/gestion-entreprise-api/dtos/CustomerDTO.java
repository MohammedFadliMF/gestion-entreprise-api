package com.example.springbootoauthjwt.dtos;


import lombok.Data;

@Data
public class CustomerDTO {
    private  Long customerId;
    private String customerName;
    private String email;
}
