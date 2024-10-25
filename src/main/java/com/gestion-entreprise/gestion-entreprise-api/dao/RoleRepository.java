package com.example.springbootoauthjwt.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
   
    List<Role> findByCompany(Company company);
    
    

}
