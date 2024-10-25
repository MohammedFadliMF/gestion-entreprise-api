package com.example.springbootoauthjwt.dtos;

import java.time.LocalDateTime;

import com.example.springbootoauthjwt.model.User;

import lombok.Data;

@Data
public class MessageDTO {
    private Long messageId;
    private String content;
    private LocalDateTime timestamp;
    private User sender;
}
