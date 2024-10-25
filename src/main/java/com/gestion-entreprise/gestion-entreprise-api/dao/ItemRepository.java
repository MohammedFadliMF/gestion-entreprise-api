package com.example.springbootoauthjwt.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.springbootoauthjwt.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
       
    public Optional<Item>  findByItemId(Long itemId);
    

    public List <Item>findAll();

    @Query("SELECT I From Item I Where I.itemId IN :itemIds")
    public Optional<List<Item>>  findAllItemsByItemIds(@Param("itemIds") List<Long> itemIds);

    // @Query("SELECT DISTINCT u From User u Where u.username LIKE %:query% OR u.email LIKE %:query%")
    // public List<User> findByQuery(@Param("query") String query);

}
