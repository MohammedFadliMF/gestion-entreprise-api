package com.example.springbootoauthjwt.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.springbootoauthjwt.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
       
    public Optional<User> findByEmail(String email);

    public Optional<User> findByUsername(String username);

    @Query("SELECT u From User u Where u.id IN :users")
    public List<User> findAllUsersByUsersIds(@Param("users") List<Long> userIds);
    
    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r.roleId IN :rolesIds")
    List<User> findAllUsersByRolesIds(@Param("rolesIds") List<Long> rolesIds);
    
    @Query("SELECT DISTINCT u From User u Where u.username LIKE %:query% OR u.email LIKE %:query%")
    public List<User> findByQuery(@Param("query") String query);

}
