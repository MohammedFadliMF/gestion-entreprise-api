package com.example.springbootoauthjwt.dtos;

import com.example.springbootoauthjwt.enums.Permition;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class RoleDTO {
    private  Long roleId;
    // @Enumerated(EnumType.STRING)
    // private Permition permition;
    private String permition;
    
    private CompanyDTO companyDTO;
}
