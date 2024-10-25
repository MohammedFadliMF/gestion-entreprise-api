package com.example.springbootoauthjwt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Tax {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taxId;
    @NonNull
    private String name;
    @NonNull
    private Double percent;
    private String description;

}
