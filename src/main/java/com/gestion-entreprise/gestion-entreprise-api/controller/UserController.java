package com.example.springbootoauthjwt.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbootoauthjwt.dao.RoleRepository;
import com.example.springbootoauthjwt.dao.UserRepository;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.mappers.Mapper;
import com.example.springbootoauthjwt.model.Role;
import com.example.springbootoauthjwt.model.User;
import com.example.springbootoauthjwt.service.RoleService;
import com.example.springbootoauthjwt.service.UserService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController

@CrossOrigin("*")

public class UserController {
  
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    private Mapper mapper;

    @GetMapping("/get-roles")
    public ResponseEntity<List<Role>> getRolesByUserId(@RequestHeader("Authorization") String token) throws UserException, RoleException {
        
        Jwt jwt=jwtDecoder.decode(token.substring(7));
       
        User user=userRepository.findByEmail(jwt.getSubject().toString()).get();
        
        List<Role> roles=roleService.findAllRolesByUserId(user.getUserId()).stream().map(rl->mapper.formRoleDTO(rl)).collect(Collectors.toList());
        return new ResponseEntity<List<Role>>(roles,HttpStatus.OK);
    }
    
    
}
