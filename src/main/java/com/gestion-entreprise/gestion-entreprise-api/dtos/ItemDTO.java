package com.example.springbootoauthjwt.dtos;

import lombok.Data;

@Data
public class ItemDTO {
    private Long itemId;
    private String itemName;
    private double price;
}
