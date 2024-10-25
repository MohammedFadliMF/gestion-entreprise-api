package com.example.springbootoauthjwt.service;

import java.util.List;

import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.CustomerException;
import com.example.springbootoauthjwt.exceptions.ItemException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Item;
import com.example.springbootoauthjwt.model.User;

public interface ItemService {

    public Item findItemById(Long itemId) throws ItemException;

    public Item createItem(User user,
            Item item, Company company) throws CompanyException, UserException, RoleException, CustomerException;

    public Item updateItem(User user, Company company, Item oldItem, Item newItem)
            throws UserException, RoleException, CompanyException, ItemException;

    public String removeItem(User user, Company company, Item item) throws ItemException, UserException, RoleException,
            CompanyException;

    public List<Item> findAll();

    public List<Item> findAllItemsInCompany(User user, Company company)
            throws UserException, RoleException, CompanyException, ItemException;

}
