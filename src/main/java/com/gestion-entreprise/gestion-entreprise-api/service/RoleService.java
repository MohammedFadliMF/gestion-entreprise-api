package com.example.springbootoauthjwt.service;


import java.util.List;

import com.example.springbootoauthjwt.dtos.RoleDTO;
import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.Role;
import com.example.springbootoauthjwt.model.User;


public interface RoleService {
    public Role registerRole(Role role) throws RoleException;
    
    public List<RoleDTO> findAllRolesByUserId(Long userId) throws RoleException, UserException;
    
    public List<Role> findAllRolesByCompanyId(Long companyId) throws RoleException,CompanyException, UserException;
    
    public List<User> findAllUsersAllowedByRoleInCompany(String roleName,Long companyId) throws RoleException, UserException;

    public List<Role> AddRoleToUser(Long userId) throws RoleException, UserException;
 
    //  public Role findRolesByIds(List <Long> roleIds) throws RoleException;
    
    
//     public Company updateCompany(Company newCompany,Company oldCompany) throws CompanyException;
    
}
