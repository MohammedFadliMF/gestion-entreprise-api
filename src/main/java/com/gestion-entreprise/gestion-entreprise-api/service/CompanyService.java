package com.example.springbootoauthjwt.service;

import java.util.List;

import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Role;
import com.example.springbootoauthjwt.model.User;


public interface CompanyService {
    public Company registerCompany(Company Company) throws CompanyException;
        
    public List<Company> findCompanysByIds(List <Long> CompanysIds) throws CompanyException;
    
    public Company findByCompanyId(Long companyId) throws CompanyException;
    
    public Company findCompanyByEmail(String email) throws CompanyException;

    public Company updateCompany(User user,Company newCompany,Company oldCompany) 
                    throws CompanyException, UserException, RoleException;
    
    public Role findRoleByName(Company company, String rolename) throws CompanyException;
    
    public List<User> getAllUsers(Company company)throws UserException, RoleException, CompanyException;
    
    public Company AddUserToCompany(User admin, User user, Company company, String permission)
            throws UserException, CompanyException, RoleException;
            
    public Company inviteUserToCompany(User admin, User user, Company company, String permission)
            throws UserException, CompanyException, RoleException;
            
    public void handleInvitationResponse(String token) throws UserException, CompanyException, RoleException;

}
