package com.example.springbootoauthjwt.model;

import lombok.Data;

@Data
public class InvoiceItem {
    private Long itemId;
    private String itemName;
    private Double price;
    private Double quantity;
}
