package com.example.springbootoauthjwt.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailStructure {
    private String subject;
    private String message;

    private String sender;
    private String to;

}
