package com.example.springbootoauthjwt.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springbootoauthjwt.dao.RoleRepository;
import com.example.springbootoauthjwt.dtos.RoleDTO;
import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.mappers.Mapper;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Role;
import com.example.springbootoauthjwt.model.User;




@Service
public class RoleServiceIMPL implements RoleService{
    @Autowired
     private  RoleRepository roleRepository;
     @Autowired
     private  UserService userService;
     @Autowired
     private  CompanyService companyService;

     @Autowired
     private Mapper mapper;

    @Override
    public Role registerRole(Role role) throws RoleException {
        Optional<Role> isRoleExist = roleRepository.findById(role.getRoleId());

        if (isRoleExist.isPresent()) {
            throw new RoleException("Role Is Already Exist");
        }
        Role newRole = new Role();
        newRole.setPermition(role.getPermition());
        newRole.setCompany(role.getCompany());
    //    newRole.setUsers(role.getUsers());

        return roleRepository.save(newRole);
        
    }

    @Override
    public List<RoleDTO> findAllRolesByUserId(Long userId) throws RoleException, UserException {
        User user=userService.findUserById(userId);
        return user.getRoles().stream().map(rl -> mapper.fromRole(rl)).collect(Collectors.toList());
    }

    @Override
    public List<User> findAllUsersAllowedByRoleInCompany(String roleName,Long companyId) throws RoleException, UserException {
        
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllUsersAllowedByRole'");
    }
    @Override
    public List<Role> AddRoleToUser(Long userId) throws RoleException, UserException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'AddRoleToUser'");
    }
    
    @Override
    public List<Role> findAllRolesByCompanyId(Long companyId) throws RoleException,CompanyException, UserException {
        Company company=companyService.findByCompanyId(companyId);
        return roleRepository.findByCompany(company);
    }
    
    
}
