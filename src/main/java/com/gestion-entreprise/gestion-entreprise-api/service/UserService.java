package com.example.springbootoauthjwt.service;

import java.util.List;

import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.User;


public interface UserService {
    public User registerUser(User user) throws UserException;
    
    public User findUserById(Long userId) throws UserException;

    public User findUserByEmail(String email) throws UserException;
    
    public List<User> findUsersByIds(List <Long> usersIds) throws UserException;
    
    public List<User> searchUser(String query) throws UserException;
    
    public User updateUser(User newUser,User oldUser) throws UserException;
    
    public List<User>  findAllUsersByRolesIds(List <Long> rolesIds) throws UserException;

}
