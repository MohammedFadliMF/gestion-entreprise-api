package com.example.springbootoauthjwt.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.springbootoauthjwt.model.Company;

public interface CompanyRepository extends JpaRepository<Company,Long>{
    public Optional<Company> findByCompanyId(Long id);

    public Optional<Company> findByCompanyName(String name);
    
    public Optional<Company> findByCompanyEmail(String email);
    
    @Query("SELECT u From Company u Where u.companyId IN :companyIds")
    public List<Company> findAllCompanyByCompanyIds(@Param("companyIds") List<Long> companyIds);

    

}
